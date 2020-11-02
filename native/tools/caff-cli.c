#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#include "caff_parser.h"

static void read_caff(const char *file_name, unsigned char **buffer, unsigned long long *size) {
    FILE *fp = fopen(file_name, "rb");
    fseek(fp, 0, SEEK_END);
    *size = ftell(fp);
    fseek(fp, 0, SEEK_SET);

    *buffer = (unsigned char *) (malloc(*size));
    fread(*buffer, 1, *size, fp);
    fclose(fp);
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
    CAFF *caff;

    read_caff(input, &buffer, &size);
    caff_parse(buffer, size, &caff);

    if(caff == NULL) {
        free(buffer);
        caff_free(caff);
        printf("Invalid caff format");
        return 1;
    }

    unsigned char *bmpBuffer;
    unsigned long long bmpSize;
    caff_preview(caff, &bmpBuffer, &bmpSize);

    if(output != NULL) {
        bmp_fo_file(bmpBuffer, bmpSize, output);
    }

    free(buffer);
    free(bmpBuffer);
    caff_free(caff);

    return 0;
}
