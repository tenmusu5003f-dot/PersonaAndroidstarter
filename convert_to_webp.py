#!/usr/bin/env python3
import os, subprocess, sys

SRC = sys.argv[1] if len(sys.argv) > 1 else "app/src/main/res"
converted = 0
for root, _, files in os.walk(SRC):
    for f in files:
        if f.lower().endswith(".png"):
            full = os.path.join(root, f)
            out = full[:-4] + ".webp"
            cmd = ["cwebp", "-q", "82", full, "-o", out]
            try:
                subprocess.check_call(cmd, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
                os.remove(full)
                converted += 1
            except Exception as e:
                print("skip", full, e)
print("converted:", converted)
