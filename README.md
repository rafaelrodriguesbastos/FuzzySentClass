# FuzzySentClass

FuzzySentClass addresses the uncertainties and inaccuracies in the sentiment analysis classification process by exploring Interval-valued Fuzzy Logic.

Sentiment analysis, especially social network analysis, is a relevant research area that consists of analyzing and extracting emotions, opinions, or attitudes from reviews of products, services, music, and movies, classifying them into positive, neutral, and negative.

FuzzySentClass was conceived for classifying tweets based on lexicon using SentiWordnet. The FuzzySentClass proposal considers a rule base acting in three stages: Fuzzification, Inference, and Defuzzification, returning as output the classification sentiment of the analyzed tweet.
  
The modeling of FuzzySentClass system was performed using the Interval Type-2 Fuzzy Logic System Toolbox module [[2]](https://ieeexplore.ieee.org/document/4295341) and Juzzy [[1]](http://juzzy.wagnerweb.net/). 

For the execution of the experiments we used this [dataset](https://www.kaggle.com/datasets/kazanova/sentiment140).

## Publications
1. [FuzzySentClass: Interval-valued fuzzy approach to the Sentiment Analysis Problem via SentiWordNet](https://ieeexplore.ieee.org/document/10309681/)

## Reference
[1] [C. Wagner, "Juzzy – A Java based Toolkit for Type-2 Fuzzy Logic", Proceedings of the IEEE Symposium Series on Computational Intelligence, Singapore, April 2013.](http://juzzy.wagnerweb.net/)

[2] [Castro, Juan R., Oscar Castillo, and Patricia Melin. "An interval type-2 fuzzy logic toolbox for control applications." 2007 IEEE international fuzzy systems conference. IEEE, 2007.](https://ieeexplore.ieee.org/document/4295341)
