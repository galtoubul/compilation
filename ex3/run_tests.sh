#! /bin/bash
for i in `ls ex3_tests/ourinputs/*.txt`; do java -jar COMPILER $i /tmp/bar > /dev/null 2>&1 ; B=`basename $i`; echo -n "$B  "; diff -y ex3_tests/ouroutputs/$B /tmp/bar; done | column -t
