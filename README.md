# JFFTW3

Java wrapper for FFTW3.

This repo does not include any FFTW libraries. See [FFTW's website](http://fftw.org/) for download and installation information.

## Overview

FFTW is a fast fourier transform library containing a large array of transform algorithms. FFTW chooses an algorithm that is the most efficient for a certain problem size and currently available system resources. The user can configure parameters of the transform to suit their program's needs.

This Java library is meant to act as an interface between native FFTW and the Java Virtual Machine. In the interest of preventing JVM crashes due to native code errors, this library implements a few checks and validations not present in FFTW itself to ensure errors occur outside native code.

## Building the JNI Shared Library

Run GNU `make` in this directory to build the JNI shared library.

Please edit the Makefile to work for your system:

 * Change `CC` to specify a C compiler.
 * Change `PLATFORM` to your platform:
   * `linux` for Linux
   * `win32` for Windows
   * `darwin` for macOS
 * Change `JAVAINCL` to the path of your Java include directory.
 * Change `FFTWINCL` to the path of your FFTW3 include directory.
 * Change `FFTWLIBDIR` to the path of your FFTW3 lib directory.
 * Change `FFTWLIBNM` to the name of your FFTW3 library.
 * Change `TARGETLIB` to your platform specific shared library naming convention:
    * `libjfftw.so` for linux
    * `jfftw.dll` for win32
    * `libjfftw.dylib` for darwin
 * Change `FFTWLIBTHREAD` to the name of the threads library you used when compiling FFTW:
    * `fftw3_omp` if you want to use OpenMP and compiled FFTW with `--enable-openmp`
    * `fftw3_threads` if you compiled FFTW using `--enable-threads`
    * Leave this blank if you compiled FFTW with `--with-combined-threads` or if you did not compile FFTW with thread support

## Building the Java Archive

Run `build.xml` as an Ant build.

It will clean, compile, document, and package the Java objects into `jfftw.jar` which can be used as a dependency in your Java projects.

### Adding jfftw as a Dependency

Include `jfftw.jar` in your Java project as you would any other `.jar` library.

The JNI shared library you built above as well as the FFTW3 shared library must be in your project's `java.library.path` directory. You may load these native libraries using the `loadLibraries` method found in the [Interface](src/jfftw/Interface.java) class.

# Usage

This library is meant to give the user as much control over FFTW as possible while maintaining most Java semantics. For instance, we can directly translate the C code below:

```C
int N = 1024;
fftw_complex *ci = fftw_malloc(sizeof(fftw_complex) * N);
fftw_complex *co = fftw_malloc(sizeof(fftw_complex) * N);
fftw_plan plan = fftw_plan_dft_1d(N, ci, co, FFTW_FORWARD, FFTW_MEASURE | FFTW_PRESERVE_INPUT);
fftw_execute(plan);
fftw_destroy_plan(plan);
```

Into Java:

```Java
int N = 1024;
DoubleBuffer ci = DirectAllocator.allocateComplex(N);
DoubleBuffer co = DirectAllocator.allocateComplex(N);
DirectPlan p = new DirectPlan(ci, co, -1, Complexity.COMPLEX_TO_COMPLEX, Flag.combine(Flag.MEASURE, Flag.PRESERVE_INPUT), null);
p.execute();
p.destroy();
```

Both these snippets will create two complex interleaved arrays of size 1024, a new plan using those arrays and user specific flags, and execute the plan.

## Datatypes

This library makes use of direct ByteBuffers available in the `java.nio` package. Direct buffers allow the JVM and JNI to access the same shared memory location reducing overhead from copying arrays back and forth between the JVM and JNI. 

The [DirectAllocator](src/jfftw/data/DirectAllocator.java) class implements two methods to allocate direct buffers using FFTW's `fftw_alloc_complex` and `fftw_alloc_real` functions:

```Java
DoubleBuffer cplx = DirectAllocator.allocateComplex(512);
DoubleBuffer real = DirectAllocator.allocateReal(512);
```

Doing so ensures maximum support for [SIMD](http://www.fftw.org/fftw3_doc/SIMD-alignment-and-fftw_005fmalloc.html) instruction sets.

## Plans

FFTW implements a [planning](http://www.fftw.org/fftw3_doc/Using-Plans.html#Using-Plans) feature which produces a plan containing "all information necessary to compute the transform, including the pointers to the input and output arrays."

In the case of this library, the [Plan](src/jfftw/planning/Plan.java) class holds the address of the `fftw_plan` in native code, and references to the direct `DoubleBuffer` objects supplied upon plan creation.

A new array execute function is provided in the [Plan](src/jfftw/planning/Plan.java) class as well. The inputs to this method must adhere to the restrictions provided in the [FFTW doc](http://www.fftw.org/fftw3_doc/New_002darray-Execute-Functions.html#New_002darray-Execute-Functions).

### Plan Creation

To create a [Plan](src/jfftw/planning/Plan.java), you must first construct input and output arrays. Note that "you must create the plan before initializing the input, because `FFTW_MEASURE` overwrites the in/out arrays. (Technically, `FFTW_ESTIMATE` does not touch your arrays, but you should always create plans first just to be sure.)"

Use the [DirectAllocator](src/jfftw/data/DirectAllocator.java) class to allocate your arrays:

```Java
int N = 4096;
DoubleBuffer ci = DirectAllocator.allocateComplex(N); // note that the input and output sizes are the same
DoubleBuffer ro = DirectAllocator.allocateReal(N);    // also note that `ci` is a DoubleBuffer twice the capacity of `ro`
```

Then you may wish to combine a few flags:

```Java
int flags = Flag.combine(Flag.PRESERVE_INPUT, Flag.MEASURE);
```

Now you can create a plan:

```Java
DirectPlan p = new DirectPlan(ci, ro, -1, Complexity.COMPLEX_TO_REAL, flags);
```

Populate your input:

```Java
for (int i = 0; i < N*2; i += 2) {
  double re = Math.random();
  double im = Math.random();
  ci.put(i, re);
  ci.put(i + 1, im);
}
```

And execute the plan:

```Java
p.execute();
```

To collect the output of the transform, gain access to the `DoubleBuffer` referenced by your output:

```Java
DoubleBuffer out = p.getOutput(); // this method will provide access to the DoubleBuffer supplied upon Plan creation only
```

And copy the data back to a `double[]`:

```Java
double[] doubles = new double[N];
out.get(doubles);
```

Or use the `DoubleBuffer` directly:

```Java
for (double d = out.get(); out.hasRemaining(); d = out.get())
  doSomething(d);
```

## Flags

From the [FFTW doc](http://www.fftw.org/fftw3_doc/Planner-Flags.html):

> All of the planner routines in FFTW accept an integer flags argument, which is a bitwise OR (‘|’) of zero or more of the flag constants defined below. These flags control the rigor (and time) of the planning process, and can also impose (or lift) restrictions on the type of transform algorithm that is employed.
>
> Planning-rigor flags
> * `FTW_ESTIMATE` specifies that, instead of actual measurements of different algorithms, a simple heuristic is used to pick a (probably sub-optimal) plan quickly. With this flag, the input/output arrays are not overwritten during planning.
> * `FFTW_MEASURE` tells FFTW to find an optimized plan by actually computing several FFTs and measuring their execution time. Depending on your machine, this can take some time (often a few seconds). `FFTW_MEASURE` is the default planning option.
> * `FFTW_PATIENT` is like `FFTW_MEASURE`, but considers a wider range of algorithms and often produces a “more optimal” plan (especially for large transforms), but at the expense of several times longer planning time (especially for large transforms).
> * `FFTW_EXHAUSTIVE` is like `FFTW_PATIENT`, but considers an even wider range of algorithms, including many that we think are unlikely to be fast, to produce the most optimal plan but with a substantially increased planning time.
> * `FFTW_WISDOM_ONLY` is a special planning mode in which the plan is only created if wisdom is available for the given problem, and otherwise a NULL plan is returned. This can be combined with other flags, e.g. ‘`FFTW_WISDOM_ONLY | FFTW_PATIENT`’ creates a plan only if wisdom is available that was created in `FFTW_PATIENT` or `FFTW_EXHAUSTIVE` mode. The `FFTW_WISDOM_ONLY` flag is intended for users who need to detect whether wisdom is available; for example, if wisdom is not available one may wish to allocate new arrays for planning so that user data is not overwritten.
>
> Algorithm-restriction flags
> * `FFTW_DESTROY_INPUT` specifies that an out-of-place transform is allowed to overwrite its input array with arbitrary data; this can sometimes allow more efficient algorithms to be employed.
> * `FFTW_PRESERVE_INPUT` specifies that an out-of-place transform must not change its input array. This is ordinarily the default, except for c2r and hc2r (i.e. complex-to-real) transforms for which `FFTW_DESTROY_INPUT` is the default. In the latter cases, passing `FFTW_PRESERVE_INPUT` will attempt to use algorithms that do not destroy the input, at the expense of worse performance; for multi-dimensional c2r transforms, however, no input-preserving algorithms are implemented and the planner will return `NULL` if one is requested.
> * `FFTW_UNALIGNED` specifies that the algorithm may not impose any unusual alignment requirements on the input/output arrays (i.e. no SIMD may be used). This flag is normally not necessary, since the planner automatically detects misaligned arrays. The only use for this flag is if you want to use the new-array execute interface to execute a given plan on a different array that may not be aligned like the original. (Using fftw_malloc makes this flag unnecessary even then. You can also use fftw_alignment_of to detect whether two arrays are equivalently aligned.)

These flags are implemented as an Enum class in [Flag](src/jfftw/enums/Flag.java).

## Wisdom

FFTW uses [wisdom](http://www.fftw.org/fftw3_doc/Wisdom.html#Wisdom) to save and reuse plans from storage.

The [Wisdom](src/jfftw/planning/Wisdom.java) class facilitates the import and export of FFTW wisdom. 

# A Note on Thread Safety

The only thread-safe routine in FFTW is `fftw_execute` and its new array execute variants. This library enforces thread safety by prepending the `synchronized` keyword to all of its native methods except for the plan execution methods. As a result, you should be able to leverage Java parallelism for execution. However, please understand that planning routines may hold locks for an extended period of time.

It is also important to note that while you may use the same plan across a number of threads, plan execution operates on the arrays it was created with by default. To use the same plan across multiple threads with different arrays consider using the new array execution method as shown below:

```Java
int N = 8192, nthreads = 8;
DoubleBuffer ci = DirectAllocator.allocateComplex(N);
DoubleBuffer co = DirectAllocator.allocateComplex(N);
int flags = Flag.combine(Flag.MEASURE);
DirectPlan plan = new DirectPlan(ci, co, -1, Complexity.COMPLEX_TO_COMPLEX, flags, null);
for (int i = 0; i < nthreads; i++) {
  new Thread(new Runnable() {
    public void run() {
      DoubleBuffer in = Interface.allocateComplex(N);
      DoubleBuffer out = Interface.allocateComplex(N);
      plan.execute(in, out);
    }
  }).start();
}
```
