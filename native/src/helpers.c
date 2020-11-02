#include "helpers.h"

unsigned short parse_2byte_integer(const unsigned char *bytes) {
    return ((unsigned) bytes[0]) << 0U |
           ((unsigned) bytes[1]) << 8U;
}

unsigned long long parse_8byte_integer(const unsigned char *bytes) {
    return  ((unsigned long long) bytes[0]) << 0U |
            ((unsigned long long) bytes[1]) << 8U |
            ((unsigned long long) bytes[2]) << 16U |
            ((unsigned long long) bytes[3]) << 24U |
            ((unsigned long long) bytes[4]) << 32U |
            ((unsigned long long) bytes[5]) << 40U |
            ((unsigned long long) bytes[6]) << 48U |
            ((unsigned long long) bytes[7]) << 56U;
}
