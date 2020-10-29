#ifndef NATIVE_CAFF_PARSER_H
#define NATIVE_CAFF_PARSER_H

#include "ciff_parser.h"

#define BLOCK_NUMBER 3
#define ID_SIZE 1
#define BLOCKLENGTH_SIZE 8
#define NUMANIM_SIZE 8
#define YEAR_SIZE 2
#define MDHM_SIZE 1
#define CREATORLENGTH_SIZE 8
#define DURATION_SIZE 8

typedef struct {
    unsigned long long header_block_length;
    unsigned long long credits_block_length;
    unsigned long long* anim_block_length;

    char magic[MAGIC_SIZE];
    unsigned long long header_size;
    unsigned long long num_anim;

    unsigned short year;
    char month;
    char day;
    char hour;
    char minute;
    unsigned long long creator_length;
    char* creator;

    unsigned long long* durations;
    CIFF** ciffs;
} CAFF;

void caff_free(CAFF *caff);
CAFF* caff_parse(const unsigned char *buffer, unsigned long long size);
void caff_preview(const CAFF *caff, unsigned char **bmp, unsigned long long *file_size);

#endif //NATIVE_CAFF_PARSER_H
