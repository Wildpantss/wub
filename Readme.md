# Wildpants-util-box (wub)

[![License](https://img.shields.io/badge/license-Apache%202.0-blue?style=flat-square)](LICENSE)

A simple CLI util-box for music production by Wildpants.

> Note: This is just a simple personal toy, have fun ^_^

## Tasks

* [freq](#freq)
* ...

### Freq

`Freq` module helps you get harmonic series of a note.
(the standard A0 frequency is also tunable)

* Example with default A = 440Hz

  ```shell
  wub freq as
  ```

  Console output:

  ```text
  ---- Harmonic series of 'A#'
  ---- Standard A = [440 Hz]

       [29.12             Hz]
       [58.25             Hz]
       [116.50            Hz]
       [233.00            Hz]
  >>   [466.00            Hz]
       [932.00            Hz]
       [1864.00           Hz]
       [3728.00           Hz]
       [7456.00           Hz]
       [14912.00          Hz]
  ```

* Example with default A != 440Hz

  ```shell
  wub freq fs 432
  ```

  Console output:
  
  ```text
  ---- Harmonic series of 'F#'
  ---- Standard A = [432 Hz]

       [22.69             Hz]
       [45.38             Hz]
       [90.75             Hz]
       [181.50            Hz]
  >>   [363.00            Hz]
       [726.00            Hz]
       [1452.00           Hz]
       [2904.00           Hz]
       [5808.00           Hz]
       [11616.00          Hz]
  ```

## Details

For more info about the app, use `help` task

Examples:

```shell
# help info for wub.
wub help
```

```shell
# help info for sub-command freq.
wub help freq
```

## Build

* [SBT](https://www.scala-sbt.org/)
* [GraalVM](https://www.graalvm.org/)
