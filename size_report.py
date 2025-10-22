#!/usr/bin/env python3
import os, sys

ROOT = sys.argv[1] if len(sys.argv) > 1 else "."
limit_mb = float(sys.argv[2]) if len(sys.argv) > 2 else 3.0  # default warn per-file > 3MB
totals = {}
largest = []

for root, _, files in os.walk(ROOT):
    for f in files:
        fp = os.path.join(root, f)
        try:
            s = os.path.getsize(fp)
        except:
            continue
        ext = os.path.splitext(f)[1].lower()
        totals[ext] = totals.get(ext, 0) + s
        largest.append((s, fp))

largest.sort(reverse=True)
print("== Top 20 largest files ==")
for s, fp in largest[:20]:
    print(f"{s/1024/1024:7.2f} MB  {fp}")

print("\n== Size by extension ==")
for ext, s in sorted(totals.items(), key=lambda x: -x[1]):
    print(f"{ext or '(noext)'}: {s/1024/1024:7.2f} MB")

warns = [fp for s,fp in largest if s/1024/1024 > limit_mb]
print(f"\nWARN files over {limit_mb:.1f}MB: {len(warns)}")
for fp in warns[:50]:
    print(" -", fp)
