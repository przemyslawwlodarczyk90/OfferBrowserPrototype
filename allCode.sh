# Zrzut wszystkich plików .java do jednego TXT (z nagłówkami ścieżek)
find . -type f -name "*.java" \
  -not -path "*/target/*" \
  -not -path "*/build/*" \
  -not -path "*/out/*" \
  -not -path "*/.idea/*" \
  -not -path "*/.git/*" \
  -print0 \
| sort -z \
| xargs -0 -I{} bash -c 'echo -e "\n\n===== {} =====\n"; cat "{}"' \
> ALL_JAVA_CODE.txt
