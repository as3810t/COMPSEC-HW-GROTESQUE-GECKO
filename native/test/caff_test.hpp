#include <cppunit/TestCase.h>
#include <cppunit/TestFixture.h>
#include <cppunit/extensions/HelperMacros.h>
#include <stdlib.h>

extern "C" {
#include "caff_parser.h"
}

class CaffTest : public CppUnit::TestFixture {
    CPPUNIT_TEST_SUITE(CaffTest);
    CPPUNIT_TEST(testCaff1);
    CPPUNIT_TEST(testCaff2);
    CPPUNIT_TEST(testCaff2bad);
    CPPUNIT_TEST(testCaff3);
    CPPUNIT_TEST_SUITE_END();

protected:
    void testCaff1(){
        check_caff("test/caff/1.caff", true);
    }
    void testCaff2(){
        check_caff("test/caff/2.caff", true);
    }
    void testCaff2bad(){
        check_caff("test/caff/2bad.caff", false);
    }
    void testCaff3(){
        check_caff("test/caff/3.caff", true);
    }

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
    void check_caff(const char *file_name, bool expected) {
        unsigned char *buffer;
        unsigned long long size;
        read_caff(file_name, &buffer, &size);
        CAFF *caff = caff_parse(buffer, size);

        if(expected) {
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
            CPPUNIT_ASSERT_MESSAGE("Failure expected", caff == NULL);
        }
        caff_free(caff);
        free(buffer);
    }
};