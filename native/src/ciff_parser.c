#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <stdbool.h>

#include "helpers.h"
#include "ciff_parser.h"

static CIFF* ciff_create() {
    CIFF *new_ciff = (CIFF*) malloc(sizeof(CIFF));
    if(new_ciff != NULL) {
        new_ciff->caption = NULL;
        new_ciff->tag_count = 0;
        new_ciff->tags = NULL;
        new_ciff->pixels = NULL;
    }
    return new_ciff;
}

void ciff_free(CIFF *ciff) {
    if(ciff != NULL) {
        free(ciff->pixels);
        for(unsigned long long i = 0; i < ciff->tag_count; i++) {
            free(ciff->tags[i]);
        }
        free(ciff->tags);
        free(ciff->caption);
        free(ciff);
    }
}

static bool ciff_check_sizes_match(unsigned long long header_size, unsigned long long content_size, unsigned long long size) {
    unsigned long long total_size;
    return !__builtin_uaddll_overflow(header_size, content_size, &total_size) && total_size == size;
}

static bool ciff_check_content_sizes_match(unsigned long long content_size, unsigned long long width, unsigned long long height) {
    unsigned long long image_pixel_count;
    unsigned long long image_pixel_component_count;

    return
        !__builtin_umulll_overflow(width, height, &image_pixel_count) &&
        !__builtin_umulll_overflow(image_pixel_count, 3U, &image_pixel_component_count) &&
        image_pixel_component_count == content_size;
}

CIFF_RES ciff_parse(const unsigned char *buffer, unsigned long long size, CIFF **ciff) {
    size_t bytes_read = 0;
    CIFF *new_ciff = ciff_create();
    if(new_ciff == NULL) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_MEMORY;
    }

    // Magic
    if (bytes_read + MAGIC_SIZE > size) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_FORMAT_ERROR;
    }
    memcpy(new_ciff->magic, buffer + bytes_read, MAGIC_SIZE);
    bytes_read += MAGIC_SIZE;
    if (strncmp(new_ciff->magic, "CIFF", MAGIC_SIZE) != 0) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_FORMAT_ERROR;
    }

    // Header size
    if (bytes_read + HEADERSIZE_SIZE > size) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_FORMAT_ERROR;
    }
    unsigned char headersize_bytes[HEADERSIZE_SIZE];
    memcpy(headersize_bytes, buffer + bytes_read, HEADERSIZE_SIZE);
    bytes_read += HEADERSIZE_SIZE;
    new_ciff->header_size = parse_8byte_integer(headersize_bytes);

    // Content size
    if (bytes_read + CONTENTSIZE_SIZE > size) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_FORMAT_ERROR;
    }
    unsigned char contentsize_bytes[CONTENTSIZE_SIZE];
    memcpy(contentsize_bytes, buffer + bytes_read, CONTENTSIZE_SIZE);
    bytes_read += CONTENTSIZE_SIZE;
    new_ciff->content_size = parse_8byte_integer(contentsize_bytes);

    // Check if header_size + content_size <= size
    if(!ciff_check_sizes_match(new_ciff->header_size, new_ciff->content_size, size)) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_FORMAT_ERROR;
    }
    if(new_ciff->header_size < 36) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_FORMAT_ERROR;
    }

    // Width
    if (bytes_read + WIDTH_SIZE > size) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_FORMAT_ERROR;
    }
    unsigned char width_bytes[WIDTH_SIZE];
    memcpy(width_bytes, buffer + bytes_read, WIDTH_SIZE);
    bytes_read += WIDTH_SIZE;
    new_ciff->width = parse_8byte_integer(width_bytes);
    if(new_ciff->width > 7680 || new_ciff->width == 0) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_SIZE_ERROR;
    }

    // Height
    if (bytes_read + HEIGHT_SIZE > size) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_FORMAT_ERROR;
    }
    unsigned char height_bytes[HEIGHT_SIZE];
    memcpy(height_bytes, buffer + bytes_read, HEIGHT_SIZE);
    bytes_read += HEIGHT_SIZE;
    new_ciff->height = parse_8byte_integer(height_bytes);
    if(new_ciff->height > 4320 || new_ciff->height == 0) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_SIZE_ERROR;
    }

    // Check if content_size == 3*width*height
    if(!ciff_check_content_sizes_match(new_ciff->content_size, new_ciff->width, new_ciff->height)) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_FORMAT_ERROR;
    }

    // Read caption
    const unsigned char *caption_end_position = (const unsigned char*) memchr(buffer + bytes_read, '\n', new_ciff->header_size - bytes_read);
    if (caption_end_position == NULL) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_FORMAT_ERROR;
    }

    unsigned long long caption_size = caption_end_position - (buffer + bytes_read);
    new_ciff->caption = (char *) malloc(caption_size + 1);
    if (new_ciff->caption == NULL) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_MEMORY;
    }
    for (unsigned long long i = 0; i < caption_size; i++) {
        if ((buffer[bytes_read + i] & ~0x7fU) != 0) {
            ciff_free(new_ciff);
            *ciff = NULL;
            return CIFF_FORMAT_ERROR;
        }

        new_ciff->caption[i] = (char) buffer[bytes_read + i];
    }
    new_ciff->caption[caption_size] = '\0';
    bytes_read += caption_size + 1;

    // Read tags

    new_ciff->tag_count = 0;
    new_ciff->tags = NULL;
    while (bytes_read < new_ciff->header_size) {
        const unsigned char *tag_end_position = (const unsigned char*) memchr(buffer + bytes_read, '\0', new_ciff->header_size - bytes_read);
        if (tag_end_position == NULL) {
            ciff_free(new_ciff);
            *ciff = NULL;
            return CIFF_FORMAT_ERROR;
        }

        unsigned long long tag_size = tag_end_position - (buffer + bytes_read);
        char *tag = (char *) malloc(tag_size + 1);
        if (tag == NULL) {
            ciff_free(new_ciff);
            *ciff = NULL;
            return CIFF_MEMORY;
        }
        for (unsigned long long i = 0; i < tag_size; i++) {
            if ((buffer[bytes_read + i] & ~0x7fU) != 0) {
                free(tag);
                ciff_free(new_ciff);
                *ciff = NULL;
                return CIFF_FORMAT_ERROR;
            }

            tag[i] = (char) buffer[bytes_read + i];
        }
        tag[tag_size] = '\0';
        bytes_read += tag_size + 1;

        if (memchr(tag, '\n', tag_size) != NULL) {
            free(tag);
            ciff_free(new_ciff);
            *ciff = NULL;
            return CIFF_FORMAT_ERROR;
        }

        char **new_tags = (char **) realloc(new_ciff->tags, (new_ciff->tag_count + 1) * sizeof(const char *));
        if(new_tags == NULL) {
            free(tag);
            ciff_free(new_ciff);
            *ciff = NULL;
            return CIFF_MEMORY;
        }
        else {
            new_ciff->tags = new_tags;
        }
        new_ciff->tag_count++;
        new_ciff->tags[new_ciff->tag_count - 1] = tag;
    }

    // Read pixels
    new_ciff->pixels = malloc(new_ciff->content_size);
    if (new_ciff->pixels == NULL) {
        ciff_free(new_ciff);
        *ciff = NULL;
        return CIFF_MEMORY;
    }
    for (unsigned long long i = 0; bytes_read < new_ciff->header_size + new_ciff->content_size && i < new_ciff->content_size; i += 3, bytes_read += 3) {
        unsigned char red = buffer[bytes_read + 0];
        unsigned char green = buffer[bytes_read + 1];
        unsigned char blue = buffer[bytes_read + 2];

        new_ciff->pixels[i + 0] = red;
        new_ciff->pixels[i + 1] = green;
        new_ciff->pixels[i + 2] = blue;
    }

    *ciff = new_ciff;
    return CIFF_OK;
}

#define BMP_FILEHEADER_SIZE 14
#define BMP_INFOHEADER_SIZE 40
#define BMP_BYTES_PER_PIXEL 3

void ciff_to_bmp(const CIFF *ciff, unsigned char **bmp, unsigned long long *file_size) {
    unsigned long long width_in_bytes = ciff->width * BMP_BYTES_PER_PIXEL;
    unsigned long long padding_size = (4 - width_in_bytes % 4) % 4;
    unsigned long long padded_width_in_bytes = width_in_bytes + padding_size;

    *file_size = BMP_FILEHEADER_SIZE + BMP_INFOHEADER_SIZE + padded_width_in_bytes * ciff->height;

    *bmp = (unsigned char *) malloc(*file_size);
    if(*bmp == NULL) {
        return;
    }

    unsigned char *img = malloc(3 * ciff->width * ciff->height);
    for(unsigned long long i = 0; i < ciff->width; i++) {
        for(unsigned long long j = 0; j < ciff->height; j++) {
            unsigned long long x = i;
            unsigned long long y = (ciff->height - 1) - j;

            unsigned char r = ciff->pixels[(x + y * ciff->width) * 3 + 0];
            unsigned char g = ciff->pixels[(x + y * ciff->width) * 3 + 1];
            unsigned char b = ciff->pixels[(x + y * ciff->width) * 3 + 2];

            img[(x + y * ciff->width) * 3 + 2] = r;
            img[(x + y * ciff->width) * 3 + 1] = g;
            img[(x + y * ciff->width) * 3 + 0] = b;
        }
    }

    for(unsigned long long j = 0; j < ciff->height; j++) {
        unsigned char *bmp_row = *bmp + BMP_FILEHEADER_SIZE + BMP_INFOHEADER_SIZE + j * padded_width_in_bytes;
        for(unsigned long long i = 0; i < ciff->width; i++) {
            bmp_row[i * 3 + 0] = img[(ciff->width * (ciff->height - j - 1) + i) * 3 + 0];
            bmp_row[i * 3 + 1] = img[(ciff->width * (ciff->height - j - 1) + i) * 3 + 1];
            bmp_row[i * 3 + 2] = img[(ciff->width * (ciff->height - j - 1) + i) * 3 + 2];
        }
        for(unsigned long long i = width_in_bytes; i < padded_width_in_bytes; i++) {
            bmp_row[i] = 0;
        }
    }

    free(img);

    unsigned char file_header[BMP_FILEHEADER_SIZE] = {
        'B', 'M',       // Signature
        0, 0, 0, 0,     // Image file size in bytes
        0, 0, 0, 0,     // Reserved
        0, 0, 0, 0      // Start of pixel array
    };

    // File size
    file_header[2] = (unsigned char)(*file_size >> 0U);
    file_header[3] = (unsigned char)(*file_size >> 8U);
    file_header[4] = (unsigned char)(*file_size >> 16U);
    file_header[5] = (unsigned char)(*file_size >> 24U);

    // Start of pixel array
    file_header[10] = BMP_FILEHEADER_SIZE + BMP_INFOHEADER_SIZE;

    for(unsigned long long i = 0; i < BMP_FILEHEADER_SIZE; i++) {
        (*bmp)[i] = file_header[i];
    }

    unsigned char info_header[BMP_INFOHEADER_SIZE] = {
        0, 0, 0, 0,     // Header size
        0, 0, 0, 0,     // Image width
        0, 0, 0, 0,     // Image height
        0, 0,           // Number of color planes
        0, 0,           // Bits per pixel
        0, 0, 0, 0,     // Compression
        0, 0, 0, 0,     // Image size
        0, 0, 0, 0,     // Horizontal resolution
        0, 0, 0, 0,     // Vertical resolution
        0, 0, 0, 0,     // Colors in color table
        0, 0, 0, 0,     // Important color count
    };

    // Header size
    info_header[0] = BMP_INFOHEADER_SIZE;

    // Width
    info_header[4] = (unsigned char)(ciff->width >> 0U);
    info_header[5] = (unsigned char)(ciff->width >> 8U);
    info_header[6] = (unsigned char)(ciff->width >> 16U);
    info_header[7] = (unsigned char)(ciff->width >> 24U);

    // Height
    info_header[8] =  (unsigned char)(ciff->height >> 0U);
    info_header[9] =  (unsigned char)(ciff->height >> 8U);
    info_header[10] = (unsigned char)(ciff->height >> 16U);
    info_header[11] = (unsigned char)(ciff->height >> 24U);

    // Number of color planes
    info_header[12] = 1;

    // Bits per pixel
    info_header[14] = BMP_BYTES_PER_PIXEL * 8;

    for(unsigned long long i = 0; i < BMP_INFOHEADER_SIZE; i++) {
        (*bmp)[BMP_FILEHEADER_SIZE + i] = info_header[i];
    }
}
