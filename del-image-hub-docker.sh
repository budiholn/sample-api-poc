#!/usr/bin/env bash

USERNAME="budiholan"
PASSWORD="dckr_pat_n5vTR_ZVXjze7JB_yyXgZsYM6SU"
ORGANIZATION="budiholan"
IMAGE="jenkins-api-poc"
TAG="<TheOldTag>"

login_data() {
cat <<EOF
{
  "username": "$USERNAME",
  "password": "$PASSWORD"
}
EOF
}

TOKEN=`curl -s -H "Content-Type: application/json" -X POST -d "$(login_data)" "https://hub.docker.com/v2/users/login/" | jq -r .token`

curl "https://hub.docker.com/v2/repositories/${ORGANIZATION}/${IMAGE}/tags/${TAG}/" -X DELETE -H "Authorization: JWT ${TOKEN}" || true
