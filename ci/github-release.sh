#!/usr/bin/env bash

# FIXME build installers

RELEASE_BODY=$(python3 ./ci/release-body.py ${GITHUB_RELEASE_VERSION})
echo "Release body:
${RELEASE_BODY}";
