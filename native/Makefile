MKDIR   	:= mkdir
RMDIR   	:= rm -r -f

BIN     	:= ./bin
OBJ     	:= ./obj
INCLUDE 	:= ./include
SRC     	:= ./src
TOOLS	  	:= ./tools
TEST    	:= ./test
REPORT		:= ./report
REPORT_HTML	:= ./report/html

SRCS    	:= $(wildcard $(SRC)/*.c)
OBJS    	:= $(patsubst $(SRC)/%.c,$(OBJ)/%.o,$(SRCS))
CAFFLIBNAME	:= caffparser
CAFFLIB		:= $(BIN)/lib$(CAFFLIBNAME).so

TEST_SRC	:= $(TEST)/test.cpp
TEST_EXE	:= $(BIN)/test

TOOL_SRCS	:= $(wildcard $(TOOLS)/*.c)
TOOL_EXES	:= $(patsubst $(TOOLS)/%.c,$(BIN)/%,$(TOOL_SRCS))

CFLAGS  	:= -Wall -W -pedantic -fPIC -fstack-protector-all
TEST_CFLAGS	:= -Wall -W -pedantic -std=c++17
COV_CFLAGS	:= -fprofile-arcs -ftest-coverage
LDFLAGS		:=
TEST_LDFLAGS:=
COV_LDLIBS	:= -lgcov
LDLIBS		:=


TEST_TARGETS = all ciff caff

define make-test-target
test-$1: $$(TEST_EXE) | $$(REPORT)
	$$(TEST_EXE) $1
endef

define make-coverage-target
coverage-$1: CFLAGS += $$(COV_CFLAGS)
coverage-$1: LDLIBS += $$(COV_LDLIBS)
coverage-$1: test-$1 | $$(REPORT_HTML)
	lcov --capture --directory $$(OBJ) --output-file $$(REPORT)/coverage.info && \
	gcov $$(OBJ)/* && \
	genhtml $$(REPORT)/coverage.info --output-directory $$(REPORT_HTML)
endef

.PHONY: all $(addprefix test-, $(TEST_TARGETS)) $(addprefix coverage-, $(TEST_TARGETS)) clean

all: $(CAFFLIB) $(TEST_EXE) $(TOOL_EXES)

$(CAFFLIB): $(OBJS) | $(BIN)
	$(CC) $(LDFLAGS) -shared -Wl,-soname,lib$(CAFFLIBNAME).so $^ -o $@ $(LDLIBS)

$(OBJ)/%.o: $(SRC)/%.c | $(OBJ)
	$(CC) $(CFLAGS) -I$(INCLUDE) -c $< -o $@

$(BIN) $(OBJ) $(REPORT) $(REPORT_HTML):
	$(MKDIR) $@

$(TEST_EXE): $(TEST_SRC) | $(CAFFLIB)
	$(CXX) $(TEST_LDFLAGS) $(TEST_CFLAGS) -I$(INCLUDE) $< -o $@ $(LDLIBS) -lcppunit -Wl,-rpath,'$$ORIGIN' -L$(BIN) -l$(CAFFLIBNAME)

$(BIN)/%: $(TOOLS)/%.c | $(CAFFLIB)
	$(CC) $(CFLAGS) -I$(INCLUDE) -o $@ $< -Wl,-rpath,'$$ORIGIN' -L$(BIN) -l$(CAFFLIBNAME)

$(foreach element, $(TEST_TARGETS), $(eval $(call make-test-target,$(element))))
$(foreach element, $(TEST_TARGETS), $(eval $(call make-coverage-target,$(element))))

test-all:
coverage-all:

clean:
	$(RMDIR) $(OBJ)/ $(BIN)/ $(REPORT)/ *.gcov
