####################################################################################
# user specific options                                                            #
####################################################################################

# c compiler
CC = gcc

# linux, win32, or darwin
PLATFORM = linux

# path to the java include directory
JAVAINCL = /usr/lib/jvm/java/include

# path to the FFTW3 include directory
FFTWINCL = /usr/local/include

# path to the FFTW3 library directory
FFTWLIBDIR = /usr/local/lib

# FFTW3 library name
# usually 'fftw3' but the precompiled win32 binary may be 'fftw3-3'
FFTWLIBNM = fftw3

# shared library target
TARGET = libjfftw.so

# FFTW thread library name
# leave this blank if you compiled FFTW with '--with-combined-threads' or if you did
# not compile with openmp or threads at all
FFTWLIBTHREAD = fftw3_omp

####################################################################################
# probably no need to change things below this line                                #
####################################################################################

SRCS = $(wildcard src/c/*.c)
OBJS = $(SRCS:.c=.o)
CFLAGS = -march=native -O3 -fPIC -Isrc/c/ -I"$(JAVAINCL)" -I"$(JAVAINCL)/$(PLATFORM)" -I"$(FFTWINCL)" -Wall -std=c11 
LDFLAGS = -shared -l$(FFTWLIBNM) -L"$(FFTWLIBDIR)" -Wall

ifeq ($(FFTWLIBTHREAD),fftw3_omp)
	CFLAGS += -fopenmp
	LDFLAGS += -lfftw3_omp
endif

ifeq ($(FFTWLIBTHREAD),fftw3_threads)
	LDFLAGS += -lfftw3_threads
endif

RM = rm -f

.PHONY: all
all: ${TARGET}

$(TARGET): $(OBJS)
	$(CC) $(LDFLAGS) -o $@ $^
	
$(SRCS:.c=.d):%.d:%.c
	$(CC) $(CFLAGS) -MM $< > $@

include $(SRCS:.c=.d)

.PHONY: clean
clean:
	-$(RM) ${TARGET} ${OBJS} $(SRCS:.c=.d)
