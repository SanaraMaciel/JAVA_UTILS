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

docker container ls &>/dev/null
[ ${?} -eq 0 ] || msg_err "Esse script deve ser executado em modo super usuario (com sudo)." "Ex: $ sudo ${0} ${1}"

infra_needed()
{
 servicos="cpqd-database"
 echo ""
 echo "--------------------------------------------------------------------------------------"
 echo "Verificando infra-estrutura de rede"
 docker network ls | grep ${network}
 if [ ${?} -eq 0 ]
 then
   docker network inspect ${network} | grep ${network} | tr -d " "
   [ ${?} -eq 0 ] || msg_err "Problema na infra-estrutura de rede, corrija e execute novamente."
 else
   msg_err "Infra-estrutura de rede nao disponivel." "Crie a rede '${network}' ou reconfigure-a no arquivo '${properties}' e execute-o novamente"
 fi
 echo "Rede '${network}' -> OK"
 echo ""
 echo "Verificando infra-estrutura de servicos (conteiners)"
 for servico in ${servicos}
 do
   docker container ps | grep ${servico}
   if [ ${?} -eq 0 ]
   then
     docker container inspect ${servico} | grep "NetworkMode" | tr -d " "
     [ ${?} -eq 0 ] || msg_err "Servico '${servico}' nao disponivel na rede '${network}'"
   else
     msg_err "O servico ${servico} nao esta ativo nessa rede ${network}. Inicie-o para continuar."
   fi
   echo "Servico '${servico}' -> OK"
	 echo ""
 done
}

run_container()
{
 infra_needed
 echo ""
 echo "--------------------------------------------------------------------------------------"
 echo "Executando o conteiner: ${image}"
 echo "docker container run --name ${name} --hostname ${hostname} --network ${network} -p ${ports} -d ${image}"
 docker container run --name ${name} --hostname ${hostname} --restart=always --network ${network} -p ${ports} -d ${image}
}

stop_container()
{
 echo ""
 echo "--------------------------------------------------------------------------------------"
 echo "Parando o conteiner: ${image}"
 echo "docker container stop ${name}"
 docker container stop ${name}
}

remove_container()
{
 echo ""
 echo "--------------------------------------------------------------------------------------"
 echo "Removendo o conteiner: ${image}"
 echo "docker container rm -f ${name}"
 docker container rm -f ${name}
}

remove_image()
{
 echo ""
 echo "--------------------------------------------------------------------------------------"
 echo "Removendo a imagem: ${image}"
 echo "docker image rm -f ${image}"
 docker image rm -f ${image}
}

start_container()
{
 echo ""
 echo "--------------------------------------------------------------------------------------"
 echo "Inicia a imagem: ${image}"
 echo "docker start ${name}"
 docker start ${name}
}

logs_container()
{
 echo ""
 echo "--------------------------------------------------------------------------------------"
 echo "Log de execucao do container: ${name}"
 echo "docker logs ${name}"
 docker logs ${name}
}

option="restart"
[ "${1}" ] && option="${1}"

case ${option} in
  "run") run_container;;
  "stop") stop_container;;
  "remove") remove_container;;
  "start") start_container;;
  "restart") stop_container; remove_container; run_container;;
  "logs") logs_container;;
  *) msg_err "Opcao desconhecida: ${option}" "Opcoes disponiveis: run, stop, remove, start, logs e restart (default)";;
esac

echo ""
