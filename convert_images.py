#!/usr/bin/env python3
import os, subprocess, sys
from PIL import Image

SRC = sys.argv[1] if len(sys.argv) > 1 else "app/src/main/res"
MAX_DIM = 1080

def downscale(path):
    try:
        im = Image.open(path)
        w,h = im.size
        if max(w,h) > MAX_DIM:
            scale = MAX_DIM / float(max(w,h))
            im = im.resize((int(w*scale), int(h*scale)), Image.LANCZOS)
            im.save(path)
            return True
    except Exception as e:
        print("downscale skip", path, e)
    return False

converted = 0
for root,_,files in os.walk(SRC):
    for f in files:
        if f.lower().endswith(".png"):
            p = os.path.join(root, f)
            downscale(p)
            out = p[:-4] + ".webp"
            try:
                subprocess.check_call(["cwebp","-q","82",p,"-o",out])
                os.remove(p)
                converted += 1
            except Exception as e:
                print("webp skip", p, e)

print("webp converted:", converted)
