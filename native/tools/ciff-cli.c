#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#include "ciff_parser.h"

static void read_ciff(const char *file_name, unsigned char **buffer, unsigned long long *size) {
    FILE *fp = fopen(file_name, "rb");
    fseek(fp, 0, SEEK_END);
    *size = ftell(fp);
    fseek(fp, 0, SEEK_SET);  /* same as rewind(f); */

    *buffer = (unsigned char *) (malloc(*size + 1));
    fread(*buffer, 1, *size, fp);
    fclose(fp);

    (*buffer)[*size] = '\0';
}

static void bmp_fo_file(unsigned char *bmp, unsigned long long file_size, const char *file_name) {
    FILE *fp = fopen(file_name, "wb");
    fwrite(bmp, 1, file_size, fp);
    fclose(fp);
}

int main(int argc, char *argv[]) {
    const char *input;
    const char *output;

    if(argc == 2) {
        input = argv[1];
        output = NULL;
    }
    else if(argc == 3) {
        input = argv[1];
        output = argv[2];
    }
    else {
        printf("Usage: cfa-cli INPUT [OUTPUT]");
        return 1;
    }

    unsigned char *buffer;
    unsigned long long size;
    CIFF *ciff;

    read_ciff(input, &buffer, &size);
    ciff_parse(buffer, size, &ciff);

    if(ciff == NULL) {
        printf("Invalid ciff format");
        return 1;
    }

    unsigned char *bmpBuffer;
    unsigned long long bmpSize;
    ciff_to_bmp(ciff, &bmpBuffer, &bmpSize);

    if(output != NULL) {
        bmp_fo_file(bmpBuffer, bmpSize, output);
    }

    free(buffer);
    free(bmpBuffer);
    ciff_free(ciff);

    return 0;
}
