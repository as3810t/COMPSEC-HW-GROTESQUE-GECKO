#include <cppunit/TestCase.h>
#include <cppunit/TestFixture.h>
#include <cppunit/extensions/HelperMacros.h>
#include <cstdlib>

extern "C" {
#include "caff_parser.h"
}

class CaffTest : public CppUnit::TestFixture {
    CPPUNIT_TEST_SUITE(CaffTest);
    CPPUNIT_TEST_PARAMETERIZED(check_caff, {
        std::make_tuple("test/caff/1.caff", true),
        std::make_tuple("test/caff/2.caff", true),
        std::make_tuple("test/caff/2bad.caff", false),
        std::make_tuple("test/caff/3.caff", false)
    });
    CPPUNIT_TEST(fuzz_caff);
    CPPUNIT_TEST_SUITE_END();

private:
    void read_caff(const char *file_name, unsigned char **buffer, unsigned long long *size) {
        FILE *fp = fopen(file_name, "rb");
        fseek(fp, 0, SEEK_END);
        *size = ftell(fp);
        fseek(fp, 0, SEEK_SET);  /* same as rewind(f); */

        *buffer = static_cast<unsigned char *>(malloc(*size + 1));
        fread(*buffer, 1, *size, fp);
        fclose(fp);

        (*buffer)[*size] = '\0';
    }
    void bmp_fo_file(unsigned char *bmp, unsigned long long file_size, const char *file_name) {
        FILE *fp = fopen(file_name, "wb");
        fwrite(bmp, 1, file_size, fp);
        fclose(fp);
    }

    void check_caff(std::tuple<const char *, bool> tuple) {
        const char * file_name = std::get<0>(tuple);
        bool expected = std::get<1>(tuple);

        unsigned char *buffer;
        unsigned long long size;
        read_caff(file_name, &buffer, &size);
        CAFF *caff;
        CAFF_RES result = caff_parse(buffer, size, &caff);

        if(expected) {
            CPPUNIT_ASSERT_EQUAL(CAFF_OK, result);
            CPPUNIT_ASSERT_MESSAGE("Success expected", caff != NULL);

            if(caff->ciffs[0]->width > 0 && caff->ciffs[0]->height > 0) {
                unsigned char *bmp_bufer;
                unsigned long long bmp_size;
                caff_preview(caff, &bmp_bufer, &bmp_size);

                char bmp_file[100] = "";
                strcat(bmp_file, file_name);
                strcat(bmp_file, ".bmp");
                bmp_fo_file(bmp_bufer, bmp_size, bmp_file);

                free(bmp_bufer);
            }
        }
        else {
            CPPUNIT_ASSERT_EQUAL(CAFF_FORMAT_ERROR, result);
            CPPUNIT_ASSERT_MESSAGE("Failure expected", caff == NULL);
        }
        caff_free(caff);
        free(buffer);
    }

    void fuzz_caff() {
        DIR *dir = opendir("test/caff-fuzz");
        for(struct dirent *ent = readdir(dir); ent != NULL; ent = readdir(dir)) {
            if(strcmp(".", ent->d_name) == 0 || strcmp("..", ent->d_name) == 0) {
                continue;
            }

            unsigned char *buffer;
            unsigned long long size;

            char filename[250];
            strcpy(filename, "test/caff-fuzz/");
            strcat(filename, ent->d_name);
            read_caff(filename, &buffer, &size);

            CAFF *caff;
            CAFF_RES result = caff_parse(buffer, size, &caff);
            CPPUNIT_ASSERT(result == CAFF_OK || result == CAFF_FORMAT_ERROR || result == CAFF_SIZE_ERROR);

            if(result == CAFF_OK) {
                unsigned char *bmp_buffer;
                unsigned long long bmp_size;
                caff_preview(caff, &bmp_buffer, &bmp_size);
                CPPUNIT_ASSERT(bmp_buffer != NULL);
                free(bmp_buffer);
            }

            caff_free(caff);
            free(buffer);
        }
        closedir(dir);
    }
};