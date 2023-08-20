package com.example

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp:
  override def run(args: List[String]): IO[ExitCode] = Greeter.greet >> IO.pure(ExitCode.Success)
