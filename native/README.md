# Native component

This is the native component of the CAFF store.

## Requirements

The component uses CPPUNIT for unit testing and *gcov* for coverage. To install these (on Debian based systems):

```
sudo apt install libcppunit-dev lcov
```

## Building instructions

To build the project issue the following command from this directory:

```
make all
``` 

## Testing

To test the project issue the following command from this directory:

```
make test-all
```

Or if you want to test only certain components:

```
make test-ciff
```

## Coverage

**NOTE:** The code must be instrumented to measure the coverage, so coverage can only be measured on a clean project, so always run `make clean` beforehand!

To measure the coverage issue the following command from this directory:

```
make clean coverage-all
```

Or if you want to measure only certain components:

```
make clean coverage-ciff
```

The coverage report will be available in the *report/html* directory.