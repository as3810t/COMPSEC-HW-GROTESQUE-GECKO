#include <cppunit/TestCase.h>
#include <cppunit/TestFixture.h>
#include <cppunit/extensions/HelperMacros.h>
#include <cstdlib>
#include <dirent.h>

extern "C" {
#include "ciff_parser.h"
}

class CiffTest : public CppUnit::TestFixture {
    CPPUNIT_TEST_SUITE(CiffTest);
    CPPUNIT_TEST_PARAMETERIZED(check_ciff, {
        std::make_tuple("test/ciff/test1.ciff", true),
        std::make_tuple("test/ciff/test2.ciff", false),
        std::make_tuple("test/ciff/test3.ciff", false),
        std::make_tuple("test/ciff/test4.ciff", false),
        std::make_tuple("test/ciff/test5.ciff", false),
        std::make_tuple("test/ciff/test6.ciff", false),
        std::make_tuple("test/ciff/test7.ciff", false),
        std::make_tuple("test/ciff/test8.ciff", false),
        std::make_tuple("test/ciff/test9.ciff", false),
        std::make_tuple("test/ciff/test10.ciff", false),
        std::make_tuple("test/ciff/test11.ciff", true),
        std::make_tuple("test/ciff/test12.ciff", false),
        std::make_tuple("test/ciff/test13.ciff", false),
        std::make_tuple("test/ciff/test14.ciff", true),
        std::make_tuple("test/ciff/test15.ciff", false),
        std::make_tuple("test/ciff/test16.ciff", true),
    });
    CPPUNIT_TEST(fuzz_ciff);
    CPPUNIT_TEST_SUITE_END();

private:
    void read_ciff(const char *file_name, unsigned char **buffer, unsigned long long *size) {
        FILE *fp = fopen(file_name, "rb");
        fseek(fp, 0, SEEK_END);
        *size = ftell(fp);
        fseek(fp, 0, SEEK_SET);  /* same as rewind(f); */

        *buffer = static_cast<unsigned char *>(malloc(*size));
        fread(*buffer, 1, *size, fp);
        fclose(fp);
    }

    void bmp_fo_file(unsigned char *bmp, unsigned long long file_size, const char *file_name) {
        FILE *fp = fopen(file_name, "wb");
        fwrite(bmp, 1, file_size, fp);
        fclose(fp);
    }

    void check_ciff(std::tuple<const char *, bool> input) {
        const char *file_name = std::get<0>(input);
        bool expected = std::get<1>(input);

        unsigned char *buffer;
        unsigned long long size;
        read_ciff(file_name, &buffer, &size);

        CIFF *ciff;
        CIFF_RES result = ciff_parse(buffer, size, &ciff);

        if(expected) {
            CPPUNIT_ASSERT_EQUAL(CIFF_OK, result);
            CPPUNIT_ASSERT_MESSAGE("Success expected", ciff != NULL);

            if(ciff->width > 0 && ciff->height > 0) {
                unsigned char *bmp_bufer;
                unsigned long long bmp_size;
                ciff_to_bmp(ciff, &bmp_bufer, &bmp_size);

                char bmp_file[100] = "";
                strcat(bmp_file, file_name);
                strcat(bmp_file, ".bmp");
                bmp_fo_file(bmp_bufer, bmp_size, bmp_file);

                free(bmp_bufer);
            }
        }
        else {
            CPPUNIT_ASSERT(result != CIFF_OK);
            CPPUNIT_ASSERT_MESSAGE("Failure expected", ciff == NULL);
        }

        ciff_free(ciff);
        free(buffer);
    }

    void fuzz_ciff() {
        DIR *dir = opendir("test/ciff-fuzz");
        for(struct dirent *ent = readdir(dir); ent != NULL; ent = readdir(dir)) {
            if(strcmp(".", ent->d_name) == 0 || strcmp("..", ent->d_name) == 0) {
                continue;
            }

            unsigned char *buffer;
            unsigned long long size;

            char filename[250];
            strcpy(filename, "test/ciff-fuzz/");
            strcat(filename, ent->d_name);
            read_ciff(filename, &buffer, &size);

            CIFF *ciff;
            CIFF_RES result = ciff_parse(buffer, size, &ciff);
            CPPUNIT_ASSERT(result == CIFF_OK || result == CIFF_FORMAT_ERROR || result == CIFF_SIZE_ERROR);

            if(result == CIFF_OK) {
                unsigned char *bmp_buffer;
                unsigned long long bmp_size;
                ciff_to_bmp(ciff, &bmp_buffer, &bmp_size);
                CPPUNIT_ASSERT(bmp_buffer != NULL);
                free(bmp_buffer);
            }

            ciff_free(ciff);
            free(buffer);
        }
        closedir(dir);
    }
};

CPPUNIT_NS::OStringStream& operator<<(CPPUNIT_NS::OStringStream& oss, std::tuple<const char *, bool> t) {
    oss << std::get<0>(t) << ": " << (std::get<1>(t) ? "correct" : "incorrect");
    return oss;
}
