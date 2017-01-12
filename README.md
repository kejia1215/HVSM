READ ME File
=============
Name: HVSM<br>
VERSION: 1.0<br>
This explains how to use the HVSM package. This package could calculate the gene or term similarity on yeast and human with cc, bp or mf category in Gene Ontology. If you hava any problems with how to use it, send an email to KeJia:<br>
<51141201043@stu.ecnu.edu.cn><br>
HVSM is a novel approach that measures GO term similarity by incorporating information from gene co-function networks in addition to using GO structure and their annotations. <br>
<br>
****************************************************************
Requirements
This package requires Windows 7 with JRE 1.8 or JDK 1.8 installed.
*****************************************************************

## How to use?
you got three files: <br>

data: data source used<br>
HVSM.jar: HVSM runnable file<br>
HVSM.ini: HVSM configuration file<br>

### Step 1  enter "cd XXX\" in command prompt. XXX is where you save the three files. For example, if you saved the files at "c:\HVSM\", just enter "cd c:\HVSM\"

### Step 2 call HVSM package. The command is 
java -jar HVSM.jar -org [orgonism] -db [GO category] [-gene] [-termset] -i [input] -o [output]<br>

 -db <arg>    C, P or F, denotes the Database of GO, only in gene pair matching mode.<br>
 -gene        Choose Gene Pair to calculate the similarity.<br>
 -h           Print help information<br>
 -i <arg>     The input file of termset or gene pair, split with<br>
              "tab".input termset need split with ",", eg: a,b,c<br>
              d,e,f<br>
 -o <arg>     The output file of similarity, if not specified, result will output to console.<br>
 -org <arg>   human or yeast, orgonism of datasets.<br>
 -termset     Choose TermSet to calculate the similarity.<br>

For example,<br>
Calculte the similarity between termset from input<br>
java -jar HVSM.jar -org human -termset -i data\termset_example.txt<br>
output will be like this:<br>
0.0<br>
0.1831020481113516<br>
1.2060201638430177<br>
0.15344968111913673<br>
0.0<br>
0.7506287674848676<br>
0.3337400599575926<br>
0.1534608211421122<br>
0.12242461565800263<br>
0.49067429375098043<br>
it corresponding to termsets put in the input file, one termset pair one line.<br>
<br>
Calculte the similarity between gene  pair from input file, and output to an specified file.<br>
java -jar HVSM.jar -org human -db C -gene -i data\gene_example.txt -o output.txt<br>
output will be like this:<br>
0.0<br>
0.1831020481113516<br>
1.2060201638430177<br>
0.15344968111913673<br>
0.0<br>
0.7506287674848676<br>
0.3337400599575926<br>
0.1534608211421122<br>
0.12242461565800263<br>
0.49067429375098043<br>
0.1899425373921889<br>
it corresponding to gene pairs put in the input file, one gene pair one line.<br>
<br>
