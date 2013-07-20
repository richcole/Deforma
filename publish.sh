#!/bin/bash

set -e -x

s3cmd put -P target/Game-0.0.1-release.zip s3://public.richcole.org/Game/Game-0.0.1-release.zip
