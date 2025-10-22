#!/usr/bin/env python3
import os, re, sys, shutil

ROOT = sys.argv[1] if len(sys.argv) > 1 else "app/src"
dry = "--dry" in sys.argv
deleted = 0

patterns = [
    re.compile(r".*Preview\.kt$"),
    re.compile(r".*Sample.*\.kt$"),
]

for root, _, files in os.walk(ROOT):
    for f in files:
        fp = os.path.join(root, f)
        if any(p.match(fp) for p in patterns):
            if dry:
                print("would delete", fp)
            else:
                os.remove(fp)
                deleted += 1

print("deleted:", deleted)
