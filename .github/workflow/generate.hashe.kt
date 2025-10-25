name: generate-asset-hashes
on:
  push:
    paths:
      - 'external_assets/**'
jobs:
  gen-hashes:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Generate hashes
        run: |
          python3 - <<'PY'
import hashlib, json, os
root = "external_assets"
out = {}
for dirpath, _, files in os.walk(root):
  for f in files:
    path = os.path.join(dirpath, f)
    rel = os.path.relpath(path, root)
    h = hashlib.sha256(open(path,'rb').read()).hexdigest()
    out[rel.replace("\\\\","/")] = h
open("expected-hashes.json","w").write(json.dumps(out, indent=2))
print("wrote expected-hashes.json")
PY
      - name: Commit expected hashes
        run: |
          git config user.name "github-actions"
          git config user.email "actions@github.com"
          mv expected-hashes.json external_assets/
          git add external_assets/expected-hashes.json || true
          git commit -m "ci: update asset hashes" || echo "no changes"
          git push || echo "push failed"
