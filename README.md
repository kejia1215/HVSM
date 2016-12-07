# An improved approach for similarity measurement of GO terms based on their intrinsic semantic  
Author: Ke Jia  
Date: 7/12/2016  
Description: HVSM is a measure to predict the functional relationship between different gene products based on Gene Ontology(GO).  

##__Dependencies:__  
	maven(3.3.9 or later)  
	java(1.8.0 or later)  
	git  

##__By using HVSM, you need to follow steps below:__  
	1. get the source code by using commandline:   
		git clone https://github.com/kejia1215/HVSM.git  
	2. if you want to import jar package into your project, use maven to compile first:  
		mvn assembly:assembly  

##__API:__  
###	1. To predict a list of gene pair, first build the ontology from a given ontology path:  
		GOntology ontology = GOntology.parseFromXml(ontologyPath);  
###	2. then build the gene pair list from gene pair file, if your dataset contains positive and negative pairs, you need two pair list:  
		this step follow Example#getGenePair(path,splitter)  
###	3. get annotations based on annotation file:  
		GeneAnnotations annotations = GeneAnnotations.parseFromFile(assocPath, db, genePairs, ontology);  
		db is the database name of annotation(such as SGD or UniportKB)  
		genePairs is the gene wanted, this parameter is to filter out gene which is useless  
		ontology is instance got from step 1  
###	4. generate a GeneMatching instance based on ontology and annotations:  
		GeneMatching matching = new GeneMatching(ontology, annotations);  
###	5. calculate the similarity on gene pairs mentioned before:  
		double sim = matching.calCCSimilarity(pair1, pair2);  
		or  
		double sim = matching.calBPSimilarity(pair1, pair2);  
		or  
		double sim = matching.calMFSimilarity(pair1, pair2);  