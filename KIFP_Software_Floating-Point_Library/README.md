1. Project Overview

The Zeus User Operations Notary (ZUON) Programming Language is a python-like interpreted
programming language designed to run on an embedded system without any hardware
floating-point support. Many such embedded systems typically do not have support for floats
in hardware. You won’t have to worry about any of this, of course, but it means that you won’t
be able to use any normal floats or doubles. You may not use any C float or double types.
Your job is to implement KIFP, which is a custom 9-bit floating-point library.
You will be implementing functions to create the KIFP 9-bit floating point library that ZUON will
be using. You will be completing six functions in src/kifp.c for the API, however, it is highly
recommended that you write a large number of helper functions.
In addition to encoding and decoding KIFP values, you will also do some native arithmetic in this
format, using the techniques covered in class, operating on kifp_t values.

2. The ZUON Programming Language

The good news is that the ZUON programming language is already written; all you have to do is
finish the API implementation for the six KIFP functions in kifp.c that ZUON will use. This
language is normally programmed interactively from the command line without any inputs, in
which case it will let you type in your operations, one per line, for it to execute.

Here is a summary of how ZUON calls your functions:

Any number entered will call your toKifp function to convert it into a KIFP Value (kifp_t)

ZUON Functions:

print() Calls your toNumber to convert a kifp_t value to a Number

display() Debug Function that will display any kifp_t value in Binary

Arithmetic Operators:

+ Calls your addKifp to add two kifp_t values and return a kifp_t result.

- Calls your subKifp to subtract two kifp_t values and return a kifp_t result.

* Calls your mulKifp to multiply two kifp_t values and return a kifp_t result.

Negation Operator:

- Calls your negateKifp to negate a kifp_t values and return a kifp_t result

ZUON supports any variable that starts with a letter: (eg. foo, B, or o_b_1)

ZUON has four different arithmetic operators: = + - *

ZUON has a negation operator: -
(eg. When you enter -2, we convert 2 first to KIFP, then we do the negation!)

ZUON has two constants: inf nan

ZUON has two functions: print() and display()

ZUON has one command: quit (exit also works)

ZUON does single-line comments: # (eg. # this is a comment)

Specification for this Project:

We’ve already written the ZUON Programming language for you and provided you with the
stubs (empty functions) for the six functions you will be writing inside of src/kifp.c
Complete this code, along with any number of helper functions that you would like to use, in
order to implement these three functions. In this project, you will be working with our custom
kifp_t type variables. These custom types are 32-bit signed ints in memory. Within these 32-
bits, you will be encoding our custom 9-bits floating point value.
Since a kifp_t type is just a standard int, you can do operations on it just like you normally
would with a signed int. (eg. shifting, masking, and other bitwise ops). Ultimately, you will be
getting the S, exp, and frac information and storing them within your kifp_t value.

KIFP Representation (kifp_t) Values:

This is the 9-bit Representation for kifp_t values:

1 bit for sign (s), 3 bits for exponent (exp) and 5 bits for fraction (frac).

KIFP is a slightly simplified version of floating-point encoding. We can support Normalized,
Denormalized, and Special (NaN or ∞) encodings. ZUON does support rounding, but only
Round-To-Zero (Truncation).

Number Struct Definition (inc/common_structs.h):

typedef struct number_struct {

 char original[255]; // Reference String for ZUON, Ignore this Field

 int is_negative; // 1 if Negative, 0 if Positive

 int is_infinity; // 1 if Infinity, 0 Otherwise

 int is_nan; // 1 if NaN, 0 Otherwise

 int whole; // 32-bit Whole Portion of Number in Binary
 // (eg. For 3.25, whole = 0x3)

 int fraction; // 32-bit Fraction Portion in Binary

 // (eg. For 3.25, frac = 0x40000000, which represents .01000000000000...)

 int precision; // Used Internally by ZUON, Ignore this Field

} Number_t;

