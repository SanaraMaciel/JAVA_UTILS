#!/bin/bash

properties=cpqd-integration-viacep.properties

msg_err()
{
  echo ""
  echo "${1}"
  [ "${2}" ] && echo "${2}"
  echo ""
  exit 1
}

[ -f ${properties} ] || msg_err "Esse script precisa do arquivo de configuracoes '${properties}' no diretorio."
source ${properties}

image=${repository}${name}:${version}

echo ""
echo "#----------------------------------------------------"
echo "# Geracao da imagem docker para execucao do artefato: ${image}"
echo "#----------------------------------------------------"
echo ""
echo "#--------------------------------------------------"
echo "# Apaga a imagem existente do container de execucao"
echo "docker image rm -f ${image}"
docker image rm -f ${image}

echo ""
echo "#-------------------------------------------------"
echo "# Construcao da imagem que ira executar o artefato"
echo "docker build --build-arg ARTIFACT_VERSION=${version} -t ${image} ."
docker build --build-arg ARTIFACT_VERSION=${version} -t ${image} .

