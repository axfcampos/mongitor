# mongitor

Monitor mongodb activity

## Current Features

You can list the current ongoing operations and kill them.

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

ENV variables:

* `DATABASE_URI`: The uri to the mongodb database

To start a web server for the application, run:

    lein run

Live code reloading

    lein figwheel

To create a standalone executable for your application simply run

    lein uberjar

## TODO

- [ ] Add github oauth authentication
- [ ] Make a better looking interface
- [ ] Add error handling
- [ ] Add testing


## How it looks

![Mongitor MVP](https://raw.githubusercontent.com/axfcampos/mongitor/master/images/mongitor-mvp.png)
