[![Build Status](https://semaphoreci.com/api/v1/projects/75abd0f0-8904-44a6-be51-b67eb1776e10/617309/badge.svg)](https://semaphoreci.com/axfcampos/mongitor)
# mongitor

Monitor mongodb activity

## Current Features

You can list the current ongoing operations and kill them.

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

If you have [homebrew](http://brew.sh/):
* `brew install leiningen`

## Running

ENV variables:

* `DATABASE_URI`: The uri to the mongodb database

To be able to visualize and kill operations you need **ADMIN** access to the database.

To start a web server for the application, run:

    lein run

Live code reloading

    lein figwheel

To create a standalone executable for your application simply run

    lein uberjar

## How it looks

![Mongitor MVP](https://raw.githubusercontent.com/axfcampos/mongitor/master/images/mongitor-mvp.png)
