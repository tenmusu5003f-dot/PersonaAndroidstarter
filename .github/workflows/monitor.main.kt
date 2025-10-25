name: Monitor main pushes

on:
  push:
    branches: [ "main" ]

permissions:
  contents: read
  issues: write

jobs:
  alert:
    runs-on: ubuntu-latest
    steps:
      - name: Get pusher
        id: pusher
        run: |
          echo "PUSHER=${{ github.actor }}" >> $GITHUB_OUTPUT

      - name: Check whitelist
        id: check
        run: |
          WHITELIST="あなたのユーザー名 other-trusted-user"
          for u in $WHITELIST; do
            if [ "${{ steps.pusher.outputs.PUSHER }}" = "$u" ]; then
              echo "ALLOWED=true" >> $GITHUB_OUTPUT
              exit 0
            fi
          done
          echo "ALLOWED=false" >> $GITHUB_OUTPUT

      - name: Create issue if not allowed
        if: steps.check.outputs.ALLOWED == 'false'
        uses: peter-evans/create-issue-from-file@v6
        with:
          title: "Unauthorized push to main by ${{ github.actor }}"
          content: |
            Unauthorized push detected.
            Repository: ${{ github.repository }}
            Ref: ${{ github.ref }}
            Actor: ${{ github.actor }}
            Commit: ${{ github.sha }}
            Please investigate and revert if malicious.
