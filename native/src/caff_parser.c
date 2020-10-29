#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <stdbool.h>

#include "caff_parser.h"

static unsigned long long parse_8byte_integer(const unsigned char *bytes) {
    return  ((unsigned long long) bytes[0]) << 0U |
            ((unsigned long long) bytes[1]) << 8U |
            ((unsigned long long) bytes[2]) << 16U |
            ((unsigned long long) bytes[3]) << 24U |
            ((unsigned long long) bytes[4]) << 32U |
            ((unsigned long long) bytes[5]) << 40U |
            ((unsigned long long) bytes[6]) << 48U |
            ((unsigned long long) bytes[7]) << 56U;
}

static unsigned short parse_2byte_integer(const unsigned char *bytes) {
    return ((unsigned short) bytes[0]) << 0U |
           ((unsigned short) bytes[1]) << 8U;
}

static CAFF* caff_create() {
    CAFF *new_caff = (CAFF*) malloc(sizeof(CAFF));
    new_caff->anim_block_length = NULL;
    new_caff->num_anim = 0;
    new_caff->creator = NULL;
    new_caff->durations = NULL;
    new_caff->ciffs = NULL;
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

CAFF* caff_parse(const unsigned char *buffer, unsigned long long size) {
    size_t bytes_read = 0;
    CAFF *new_caff = caff_create();
    unsigned long long ciffs_parsed = 0;
    bool credits_parsed = false;
    //HEADER
    if (bytes_read + ID_SIZE > size){
        caff_free(new_caff);
        return NULL;
    }
    unsigned char id1_byte[ID_SIZE];
    memcpy(id1_byte, buffer + bytes_read, ID_SIZE);
    bytes_read += ID_SIZE;
    if (id1_byte[0] != 1){
        caff_free(new_caff);
        return NULL;
    }
    //Block 1
    if (bytes_read + BLOCKLENGTH_SIZE > size){
        caff_free(new_caff);
        return NULL;
    }
    unsigned char blocklength_bytes[BLOCKLENGTH_SIZE];
    memcpy(blocklength_bytes, buffer + bytes_read, BLOCKLENGTH_SIZE);
    bytes_read += BLOCKLENGTH_SIZE;
    new_caff->header_block_length = parse_8byte_integer(blocklength_bytes);
    //Magic
    if (bytes_read + MAGIC_SIZE > size) {
        caff_free(new_caff);
        return NULL;
    }
    memcpy(new_caff->magic, buffer + bytes_read, MAGIC_SIZE);
    bytes_read += MAGIC_SIZE;
    if (strncmp(new_caff->magic, "CAFF", MAGIC_SIZE) != 0) {
        caff_free(new_caff);
        return NULL;
    }
    // Header size
    if (bytes_read + HEADERSIZE_SIZE > size) {
        caff_free(new_caff);
        return NULL;
    }
    unsigned char headersize_bytes[HEADERSIZE_SIZE];
    memcpy(headersize_bytes, buffer + bytes_read, HEADERSIZE_SIZE);
    bytes_read += HEADERSIZE_SIZE;
    new_caff->header_size = parse_8byte_integer(headersize_bytes);

    if (new_caff->header_block_length != new_caff->header_size){
        caff_free(new_caff);
        return NULL;
    }
    // NumAnim size
    if (bytes_read + NUMANIM_SIZE > size) {
        caff_free(new_caff);
        return NULL;
    }
    unsigned char numanimsize_bytes[NUMANIM_SIZE];
    memcpy(numanimsize_bytes, buffer + bytes_read, NUMANIM_SIZE);
    bytes_read += NUMANIM_SIZE;
    new_caff->num_anim = parse_8byte_integer(numanimsize_bytes);

    new_caff->anim_block_length = (unsigned long long *) malloc(sizeof(unsigned long long) * new_caff->num_anim);
    new_caff->durations = (unsigned long long *) malloc(sizeof(unsigned long long) * new_caff->num_anim);
    new_caff->ciffs = (CIFF **) malloc(sizeof(CIFF *) * new_caff->num_anim);
    for (unsigned long long i = 0; i < new_caff->num_anim; ++i) {
        new_caff->ciffs[i] = NULL;
    }

    while(ciffs_parsed < new_caff->num_anim || !credits_parsed){
        if (bytes_read + ID_SIZE > size) {
            caff_free(new_caff);
            return NULL;
        }
        unsigned char id_byte[ID_SIZE];
        memcpy(id_byte, buffer + bytes_read, ID_SIZE);
        bytes_read += ID_SIZE;
        if ((id_byte[0] == 2 && credits_parsed) || (id_byte[0] == 3 && new_caff->num_anim <= ciffs_parsed)){
            caff_free(new_caff);
            return NULL;
        }
        //CREDITS
        if (id_byte[0] == 2){
            //Block 2
            if (bytes_read + BLOCKLENGTH_SIZE > size){
                caff_free(new_caff);
                return NULL;
            }
            unsigned char blocklength2_bytes[BLOCKLENGTH_SIZE];
            memcpy(blocklength2_bytes, buffer + bytes_read, BLOCKLENGTH_SIZE);
            bytes_read += BLOCKLENGTH_SIZE;
            new_caff->credits_block_length = parse_8byte_integer(blocklength2_bytes);
            //Year
            if (bytes_read + YEAR_SIZE > size){
                caff_free(new_caff);
                return NULL;
            }
            unsigned char year_bytes[YEAR_SIZE];
            memcpy(year_bytes, buffer + bytes_read, YEAR_SIZE);
            bytes_read += YEAR_SIZE;
            new_caff->year = parse_2byte_integer(year_bytes);
            //Month
            if (bytes_read + MDHM_SIZE > size){
                caff_free(new_caff);
                return NULL;
            }
            unsigned char month_byte[MDHM_SIZE];
            memcpy(month_byte, buffer + bytes_read, MDHM_SIZE);
            bytes_read += MDHM_SIZE;
            new_caff->month = month_byte[0];
            //Day
            if (bytes_read + MDHM_SIZE > size){
                caff_free(new_caff);
                return NULL;
            }
            unsigned char day_byte[MDHM_SIZE];
            memcpy(day_byte, buffer + bytes_read, MDHM_SIZE);
            bytes_read += MDHM_SIZE;
            new_caff->day = day_byte[0];
            //Hour
            if (bytes_read + MDHM_SIZE > size){
                caff_free(new_caff);
                return NULL;
            }
            unsigned char hour_byte[MDHM_SIZE];
            memcpy(hour_byte, buffer + bytes_read, MDHM_SIZE);
            bytes_read += MDHM_SIZE;
            new_caff->hour = hour_byte[0];
            //Minute
            if (bytes_read + MDHM_SIZE > size){
                caff_free(new_caff);
                return NULL;
            }
            unsigned char minute_byte[MDHM_SIZE];
            memcpy(minute_byte, buffer + bytes_read, MDHM_SIZE);
            bytes_read += MDHM_SIZE;
            new_caff->minute = minute_byte[0];
            //Creator Length
            if (bytes_read + CREATORLENGTH_SIZE > size){
                caff_free(new_caff);
                return NULL;
            }
            unsigned char creatorlength_bytes[CREATORLENGTH_SIZE];
            memcpy(creatorlength_bytes, buffer + bytes_read, CREATORLENGTH_SIZE);
            bytes_read += CREATORLENGTH_SIZE;
            new_caff->creator_length = parse_8byte_integer(creatorlength_bytes);
            if (new_caff->credits_block_length != (new_caff->creator_length + CREATORLENGTH_SIZE + YEAR_SIZE + 4*MDHM_SIZE)){
                caff_free(new_caff);
                return NULL;
            }
            new_caff->creator = (char*) malloc(new_caff->creator_length + 1);
            for (unsigned long long i = 0; i < new_caff->creator_length; i++) {
                if ((buffer[bytes_read + i] & ~0x7fU) != 0) {
                    caff_free(new_caff);
                    return NULL;
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
                return NULL;
            }
            unsigned char blocklength3_bytes[BLOCKLENGTH_SIZE];
            memcpy(blocklength3_bytes, buffer + bytes_read, BLOCKLENGTH_SIZE);
            bytes_read += BLOCKLENGTH_SIZE;
            new_caff->anim_block_length[ciffs_parsed] = parse_8byte_integer(blocklength3_bytes);

            //Duration
            if (bytes_read + DURATION_SIZE > size){
                caff_free(new_caff);
                return NULL;
            }
            unsigned char duration_bytes[DURATION_SIZE];
            memcpy(duration_bytes, buffer + bytes_read, DURATION_SIZE);
            bytes_read += DURATION_SIZE;
            new_caff->durations[ciffs_parsed] = parse_8byte_integer(duration_bytes);
            //CIFF
            new_caff->ciffs[ciffs_parsed] = ciff_parse(buffer + bytes_read, size - bytes_read);
            if (new_caff->ciffs[ciffs_parsed] == NULL){
                caff_free(new_caff);
                return NULL;
            }
            bytes_read += (new_caff->ciffs[ciffs_parsed]->header_size + new_caff->ciffs[ciffs_parsed]->content_size);

            ciffs_parsed++;
        } else {
            caff_free(new_caff);
            return NULL;
        }
    }

    return new_caff;
}

void caff_preview(const CAFF *caff, unsigned char **bmp, unsigned long long *file_size) {
    ciff_to_bmp(caff->ciffs[0], bmp, file_size);
}