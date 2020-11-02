#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <stdbool.h>

#include "helpers.h"
#include "caff_parser.h"

static CAFF* caff_create() {
    CAFF *new_caff = (CAFF*) malloc(sizeof(CAFF));
    if(new_caff != NULL) {
        new_caff->anim_block_length = NULL;
        new_caff->num_anim = 0;
        new_caff->creator = NULL;
        new_caff->durations = NULL;
        new_caff->ciffs = NULL;
    }
    return new_caff;
}

void caff_free(CAFF *caff){
    if (caff != NULL){
        free(caff->anim_block_length);
        free(caff->creator);
        free(caff->durations);
        for (unsigned long long i = 0; i < caff->num_anim; i++) {
            ciff_free(caff->ciffs[i]);
        }
        free(caff->ciffs);
        free(caff);
    }
}

CAFF_RES caff_parse(const unsigned char *buffer, unsigned long long size, CAFF **caff) {
    size_t bytes_read = 0;
    CAFF *new_caff = caff_create();
    if(new_caff == NULL) {
        caff_free(new_caff);
        *caff = NULL;
        return CAFF_MEMORY;
    }

    unsigned long long ciffs_parsed = 0;
    bool credits_parsed = false;

    //HEADER
    if (bytes_read + ID_SIZE > size){
        caff_free(new_caff);
        *caff = NULL;
        return CAFF_FORMAT_ERROR;
    }
    unsigned char id1_byte[ID_SIZE];
    memcpy(id1_byte, buffer + bytes_read, ID_SIZE);
    bytes_read += ID_SIZE;
    if (id1_byte[0] != 1){
        caff_free(new_caff);
        *caff = NULL;
        return CAFF_FORMAT_ERROR;
    }

    //Block 1
    if (bytes_read + BLOCKLENGTH_SIZE > size){
        caff_free(new_caff);
        *caff = NULL;
        return CAFF_FORMAT_ERROR;
    }
    unsigned char blocklength_bytes[BLOCKLENGTH_SIZE];
    memcpy(blocklength_bytes, buffer + bytes_read, BLOCKLENGTH_SIZE);
    bytes_read += BLOCKLENGTH_SIZE;
    new_caff->header_block_length = parse_8byte_integer(blocklength_bytes);

    //Magic
    if (bytes_read + MAGIC_SIZE > size) {
        caff_free(new_caff);
        *caff = NULL;
        return CAFF_FORMAT_ERROR;
    }
    memcpy(new_caff->magic, buffer + bytes_read, MAGIC_SIZE);
    bytes_read += MAGIC_SIZE;
    if (strncmp(new_caff->magic, "CAFF", MAGIC_SIZE) != 0) {
        caff_free(new_caff);
        *caff = NULL;
        return CAFF_FORMAT_ERROR;
    }

    // Header size
    if (bytes_read + HEADERSIZE_SIZE > size) {
        caff_free(new_caff);
        *caff = NULL;
        return CAFF_FORMAT_ERROR;
    }
    unsigned char headersize_bytes[HEADERSIZE_SIZE];
    memcpy(headersize_bytes, buffer + bytes_read, HEADERSIZE_SIZE);
    bytes_read += HEADERSIZE_SIZE;
    new_caff->header_size = parse_8byte_integer(headersize_bytes);

    if (new_caff->header_block_length != new_caff->header_size){
        caff_free(new_caff);
        *caff = NULL;
        return CAFF_FORMAT_ERROR;
    }

    // NumAnim size
    if (bytes_read + NUMANIM_SIZE > size) {
        caff_free(new_caff);
        *caff = NULL;
        return CAFF_FORMAT_ERROR;
    }
    unsigned char numanimsize_bytes[NUMANIM_SIZE];
    memcpy(numanimsize_bytes, buffer + bytes_read, NUMANIM_SIZE);
    bytes_read += NUMANIM_SIZE;
    new_caff->num_anim = parse_8byte_integer(numanimsize_bytes);

    new_caff->anim_block_length = (unsigned long long *) malloc(sizeof(unsigned long long) * new_caff->num_anim);
    if(new_caff->anim_block_length == NULL) {
        caff_free(new_caff);
        *caff = NULL;
        return CAFF_MEMORY;
    }

    new_caff->durations = (unsigned long long *) malloc(sizeof(unsigned long long) * new_caff->num_anim);
    if(new_caff->durations == NULL) {
        caff_free(new_caff);
        *caff = NULL;
        return CAFF_MEMORY;
    }

    new_caff->ciffs = (CIFF **) malloc(sizeof(CIFF *) * new_caff->num_anim);
    if(new_caff->ciffs == NULL) {
        caff_free(new_caff);
        *caff = NULL;
        return CAFF_MEMORY;
    }

    for (unsigned long long i = 0; i < new_caff->num_anim; ++i) {
        new_caff->ciffs[i] = NULL;
    }

    while(ciffs_parsed < new_caff->num_anim || !credits_parsed){
        if (bytes_read + ID_SIZE > size) {
            caff_free(new_caff);
            *caff = NULL;
            return CAFF_FORMAT_ERROR;
        }
        unsigned char id_byte[ID_SIZE];
        memcpy(id_byte, buffer + bytes_read, ID_SIZE);
        bytes_read += ID_SIZE;
        if ((id_byte[0] == 2 && credits_parsed) || (id_byte[0] == 3 && new_caff->num_anim <= ciffs_parsed)){
            caff_free(new_caff);
            *caff = NULL;
            return CAFF_FORMAT_ERROR;
        }

        //CREDITS
        if (id_byte[0] == 2){
            //Block 2
            if (bytes_read + BLOCKLENGTH_SIZE > size){
                caff_free(new_caff);
                *caff = NULL;
                return CAFF_FORMAT_ERROR;
            }
            unsigned char blocklength2_bytes[BLOCKLENGTH_SIZE];
            memcpy(blocklength2_bytes, buffer + bytes_read, BLOCKLENGTH_SIZE);
            bytes_read += BLOCKLENGTH_SIZE;
            new_caff->credits_block_length = parse_8byte_integer(blocklength2_bytes);

            //Year
            if (bytes_read + YEAR_SIZE > size){
                caff_free(new_caff);
                *caff = NULL;
                return CAFF_FORMAT_ERROR;
            }
            unsigned char year_bytes[YEAR_SIZE];
            memcpy(year_bytes, buffer + bytes_read, YEAR_SIZE);
            bytes_read += YEAR_SIZE;
            new_caff->year = parse_2byte_integer(year_bytes);

            //Month
            if (bytes_read + MDHM_SIZE > size){
                caff_free(new_caff);
                *caff = NULL;
                return CAFF_FORMAT_ERROR;
            }
            unsigned char month_byte[MDHM_SIZE];
            memcpy(month_byte, buffer + bytes_read, MDHM_SIZE);
            bytes_read += MDHM_SIZE;

            new_caff->month = (char) month_byte[0];
            //Day
            if (bytes_read + MDHM_SIZE > size){
                caff_free(new_caff);
                *caff = NULL;
                return CAFF_FORMAT_ERROR;
            }
            unsigned char day_byte[MDHM_SIZE];
            memcpy(day_byte, buffer + bytes_read, MDHM_SIZE);
            bytes_read += MDHM_SIZE;
            new_caff->day = (char) day_byte[0];

            //Hour
            if (bytes_read + MDHM_SIZE > size){
                caff_free(new_caff);
                *caff = NULL;
                return CAFF_FORMAT_ERROR;
            }
            unsigned char hour_byte[MDHM_SIZE];
            memcpy(hour_byte, buffer + bytes_read, MDHM_SIZE);
            bytes_read += MDHM_SIZE;
            new_caff->hour = (char) hour_byte[0];

            //Minute
            if (bytes_read + MDHM_SIZE > size){
                caff_free(new_caff);
                *caff = NULL;
                return CAFF_FORMAT_ERROR;
            }
            unsigned char minute_byte[MDHM_SIZE];
            memcpy(minute_byte, buffer + bytes_read, MDHM_SIZE);
            bytes_read += MDHM_SIZE;
            new_caff->minute = (char) minute_byte[0];

            //Creator Length
            if (bytes_read + CREATORLENGTH_SIZE > size){
                caff_free(new_caff);
                *caff = NULL;
                return CAFF_FORMAT_ERROR;
            }
            unsigned char creatorlength_bytes[CREATORLENGTH_SIZE];
            memcpy(creatorlength_bytes, buffer + bytes_read, CREATORLENGTH_SIZE);
            bytes_read += CREATORLENGTH_SIZE;
            new_caff->creator_length = parse_8byte_integer(creatorlength_bytes);
            if (new_caff->credits_block_length != (new_caff->creator_length + CREATORLENGTH_SIZE + YEAR_SIZE + 4*MDHM_SIZE)){
                caff_free(new_caff);
                *caff = NULL;
                return CAFF_FORMAT_ERROR;
            }

            new_caff->creator = (char*) malloc(new_caff->creator_length + 1);
            if (new_caff->creator == NULL) {
                caff_free(new_caff);
                *caff = NULL;
                return CAFF_MEMORY;
            }

            for (unsigned long long i = 0; i < new_caff->creator_length; i++) {
                if ((buffer[bytes_read + i] & ~0x7fU) != 0) {
                    caff_free(new_caff);
                    *caff = NULL;
                    return CAFF_FORMAT_ERROR;
                }

                new_caff->creator[i] = (char) buffer[bytes_read + i];
            }
            new_caff->creator[new_caff->creator_length] = '\0';
            bytes_read += new_caff->creator_length;
            credits_parsed = true;

        //ANIMATION
        } else if (id_byte[0] == 3){
            //Block 3
            if (bytes_read + BLOCKLENGTH_SIZE > size){
                caff_free(new_caff);
                *caff = NULL;
                return CAFF_FORMAT_ERROR;
            }
            unsigned char blocklength3_bytes[BLOCKLENGTH_SIZE];
            memcpy(blocklength3_bytes, buffer + bytes_read, BLOCKLENGTH_SIZE);
            bytes_read += BLOCKLENGTH_SIZE;
            new_caff->anim_block_length[ciffs_parsed] = parse_8byte_integer(blocklength3_bytes);

            //Duration
            if (bytes_read + DURATION_SIZE > size){
                caff_free(new_caff);
                *caff = NULL;
                return CAFF_FORMAT_ERROR;
            }
            unsigned char duration_bytes[DURATION_SIZE];
            memcpy(duration_bytes, buffer + bytes_read, DURATION_SIZE);
            bytes_read += DURATION_SIZE;
            new_caff->durations[ciffs_parsed] = parse_8byte_integer(duration_bytes);

            //CIFF
            CIFF_RES ciff_result = ciff_parse(buffer + bytes_read, size - bytes_read, &new_caff->ciffs[ciffs_parsed]);
            switch (ciff_result) {
                case CIFF_FORMAT_ERROR:
                    caff_free(new_caff);
                    *caff = NULL;
                    return CAFF_FORMAT_ERROR;
                case CIFF_SIZE_ERROR:
                    caff_free(new_caff);
                    *caff = NULL;
                    return CAFF_SIZE_ERROR;
                case CIFF_MEMORY:
                    caff_free(new_caff);
                    *caff = NULL;
                    return CAFF_MEMORY;
                case CIFF_OK:
                    // Do nothing
                    break;
            }
            bytes_read += (new_caff->ciffs[ciffs_parsed]->header_size + new_caff->ciffs[ciffs_parsed]->content_size);

            ciffs_parsed++;
        } else {
            caff_free(new_caff);
            *caff = NULL;
            return CAFF_FORMAT_ERROR;
        }
    }

    *caff = new_caff;
    return CAFF_OK;
}

void caff_preview(const CAFF *caff, unsigned char **bmp, unsigned long long *file_size) {
    ciff_to_bmp(caff->ciffs[0], bmp, file_size);
}
