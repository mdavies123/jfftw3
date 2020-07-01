# JFFTW

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

## Usage

### Datatypes

This library implements two objects, [Complex](src/jfftw/Complex.java) and [Real](src/jfftw/Real.java), to represent the data you wish to transform.

The `Complex` object is backed by a `DoubleBuffer` with a capacity twice the size of the `Complex` object's size. As FFTW expects arrays to be [interleaved](http://www.fftw.org/fftw3_doc/Complex-numbers.html#Complex-numbers), the imaginary part of the complex number is understood to be stored in the next memory location from the real part. Therefore, the capacity of a `Complex` object's `DoubleBuffer` is twice the size of the `Complex` object's size. You may access the real and imaginary parts of the `Complex` object's `DoubleBuffer` using the template below:

```Java
int N = 500;
Complex cplx = new Complex(N);
DoubleBuffer buff = cplx.get();
for (int i = 0; i < N * 2; i += 2) {
  double re = buff.get(i);
  double im = buff.get(i + 1);
}
```

The `Real` object is backed by a `DoubleBuffer` with a capacity equal to the `Real` object's size. As there is no imaginary part to this `Real` object, the capacity of its `DoubleBuffer` is equal to its size. You may access real values using the template below:

```Java
int N = 500;
Real real = new Real(N);
DoubleBuffer buff = real.get();
for (int i = 0; i < N; i++) {
  double re = buff.get(i);
}
```

Both the `Complex` and `Real` objects extend the package-private `Value` class, and can be passed to the `Plan` constructor as shown in the next section.

### Plans

FFTW implements a [planning](http://www.fftw.org/fftw3_doc/Using-Plans.html#Using-Plans) feature which produces a plan containing "all information necessary to compute the transform, including the pointers to the input and output arrays."

In the case of this library, the [Plan](src/jfftw/Plan.java) class holds the address of the `fftw_plan` in native code, and references to direct `DoubleBuffers` which contain the complex or real data for transformation. The memory allocated to these `DoubleBuffers` in the JVM will be the same memory accessible to FFTW in native code. This reduces the overhead of copying arrays back and forth between  native code and the JVM.

New array execute functions are provided in the [Plan](src/jfftw/Plan.java) class. The inputs to these methods must adhere to the restrictions provided in the [FFTW doc](http://www.fftw.org/fftw3_doc/New_002darray-Execute-Functions.html#New_002darray-Execute-Functions).

#### Plan Creation

To create a [Plan](src/jfftw/Plan.java), you must first construct input and output arrays. Note that "you must create the plan before initializing the input, because FFTW_MEASURE overwrites the in/out arrays. (Technically, FFTW_ESTIMATE does not touch your arrays, but you should always create plans first just to be sure.)"

Use the [Complex](src/jfftw/Complex.java) or [Real](src/jfftw/Real.java) classes to allocate your arrays:

```Java
int N = 4096; // N is the size of the transform
Complex ci = new Complex(N); // note that the input and output sizes are the same
Real ro = new Real(N); // also note that ci is backed by a DoubleBuffer twice the size of ro
```

Then you may wish to combine a few flags:

```Java
int flags = Flag.combine(Flag.PRESERVE_INPUT, Flag.MEASURE);
```

Now you can create a plan:

```Java
Plan p = new Plan(ci, ro, Plan.Sign.NEGATIVE, flags, N);
```

Populate your input:

```Java
double[] signal = new double[N * 2]; // note that the complex input is twice the size of the transform
double re, im;
for (int i = 0; i < signal.length; i += 2) {
  re = Math.random();
  im = Math.random();
  signal[i] = re;
  signal[i + 1] = im;
}
DoubleBuffer input = ci.get();
input.put(signal);
```

And execute the plan:

```Java
p.execute();
```

To collect the output of the transform, gain access to the `DoubleBuffer` referenced by your output:

```Java
DoubleBuffer out = ro.get();
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

### Flags

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

These flags are implemented as an Enum class in [Flag](src/jfftw/Flag.java).

### Wisdom

FFTW uses [wisdom](http://www.fftw.org/fftw3_doc/Wisdom.html#Wisdom) to save and reuse plans from storage.

The [Wisdom](src/jfftw/Wisdom.java) class facilitates the import and export of FFTW wisdom. 

### Examples

See the [examples package](src/jfftw/examples/) for some other use cases.

### Guru Interface

FFTW implements a [Guru Interface](http://www.fftw.org/fftw3_doc/Guru-Interface.html) to offer maximum flexibility over the transforms FFTW computes.

This interface is implemented in this library by the [Guru](src/jfftw/Guru.java) and [Guru64](src/jfftw/Guru64.java) classes. The Guru Interface introduces a new fftw_iodim datastructure implemented in the `Guru.Dimension` subclass. You must pass two arrays of these dimensions to the Guru interface when creating a plan. The first array describes the transform dimensions, while the second array describes the [vector](http://www.fftw.org/fftw3_doc/Guru-vector-and-transform-sizes.html#Guru-vector-and-transform-sizes) dimensions.

You must create Guru Plans statically like so:

```Java
int n0 = 4096, n1 = 32, n2 = 16;
Complex ci = new Complex(n0 * n1 * n2);
Plan.Dimension[] transform = Guru.makeDimensions(new int[] {n0, n1, n2}, new int[] {1, 1, 1}, new int[] {1, 1, 1});
Plan.Dimension[] vector = Guru.makeDimensions(new int[0], new int[0], new int[0]); // only one transform so rank = 0
Plan p = Guru.plan(transform, vector, ci, ci, sign, flags);
```

# A Note on Thread Safety

The only thread-safe routine in FFTW is `fftw_execute` and its new array execute variants. This library enforces thread safety by prepending the `synchronized` keyword to all of its native methods except for the plan execution methods. As a result, you should be able to leverage Java parallelism for execution. However, please understand that planning routines may hold locks for an extended period of time.

It is also important to note that while you may use the same plan across a number of threads, plan execution operates on the arrays it was created with by default. To use the same plan across multiple threads with different arrays please use the new array execution methods as shown below:

```Java
int N = 8192, nthreads = 8;
Complex in = new Complex(N);
Complex out = new Complex(N);
int flags = Flag.combine(Flag.MEASURE);
Plan p = new Plan(in, out, Plan.Sign.NEGATIVE, flags, N);
for (int i = 0; i < nthreads; i++) {
  new Thread(new Runnable() {
    public void run() {
      Complex input = new Complex(N);
      Complex output = new Complex(N);
        plan.execute(input, output);
    }
  }).start();
}
```
