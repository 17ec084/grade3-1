<?php

function lcrp_main($args)
//�u�֐��S�́v�̖��O�͕K��lcrp_main�Ƃ��邱�ƁB
//�������͂��̗L������Ɋւ�炸$args�Ƃ���K�v������B
{
//����
$pow=$args[0];
$num=$args[1];
$return=[];
$return[1]='';
$return[2]=false;
//���������܂�

/*
function getNextPow($pow, $num)
{
  //$pow����1�����Z�������̂��f�����ǂ������q�ׂ�B
  //�܂�$j�́A$pow��$num�̉���ł��邩�������B(print���Ɏg���B)
*/

    $return[0] = $pow*$num;
    return $return;
}