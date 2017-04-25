from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np
import pandas as pd

from scipy.io import mmwrite as matrix_write
import csv
from multiprocessing.dummy import Pool as ThreadPool
import gzip
import timeit
from os import listdir
import os
import pickle as pk
pool = ThreadPool(60)
# file_pool = ThreadPool(1)
# writing_pool = ThreadPool(1)
start = timeit.default_timer()

#Your statements here
#path = "karthik-gcloud-data/2015_08/"
# path = ""
path ="data/"

#files_list = [f for f in listdir(path)]
files_list = ["2015_08_000000000029"]
# files_list = ["sample_filtering_text.csv"]

class vectorization():
    def __init__(self,filename):
        df = pd.read_csv(filename)
        self.filename = filename
        #self.comments = list(df["body"])
        self.filtered = []
        self.comments = df["body"][:100]
        self.chunk_size = 1000


    def checkValid(self,i):
	if not isinstance(i, str)  :
	     return None
        if(len(i)> 2):
            # print i
#            try:
#                if TextBlob(i).detect_language() is "en":
#                    return str(i).strip()
#            except:
	    body = str(i)
#                print isinstance(i, unicode)
#		print len(body), i
            cmm = ""
            for id in xrange(len(body)):
                i = body[id]

                if isinstance(i, unicode):
                    cmm += str(i.encode('ascii', 'ignore'))
                else:
                    cmm += str(i)
                #print "Transformed as: ", cmm
            return cmm.strip()


    def save(self,line):
        self.writer.writerows(np.array(line))

    def filewriting(self):
        # writing_pool.map(self.checkValid,self.comments)

        vectorizer = TfidfVectorizer(stop_words='english', norm='l2', sublinear_tf=True)
        tfidf_matrix = vectorizer.fit_transform(self.filtered)
        #print tfidf_matrix

        #write = pd.DataFrame(data = tfidf_matrix)

        rows,col = tfidf_matrix.shape
        matrix_write(self.filename + "_out",tfidf_matrix)


        # f = open(self.filename + "_out.csv", "wb")
        # self.writer = csv.writer(f)

        # for i in range(0,rows,self.chunk_size):
        #     if i+self.chunk_size < rows:
        #         l = i + self.chunk_size
        #     else:
        #         l = rows
        #
        #     self.writer.writerows(tfidf_matrix[i:l].toarray())

        # print "Transformation is done ..."
        # f = open(self.path + self.filename + "_out.csv", "wb")
        # self.writer = csv.writer(f)
        #
        #
        # pool.map(self.save,tfidf_matrix.todense())

# file_pool.map(vectorization,files_list)
fname = "".join([path, files_list[0]])
v  = vectorization(fname)
print "processing data: (count): ", len(v.comments)
if os._exists(fname+"_filtered.pk"):
    filtered = pk.load(fname + "_filtered.pk")
else:
    filtered = pool.map(v.checkValid, v.comments)
    pk.dump(filtered, open(fname+"_filtered.pk", "wb"))

print "filtered data"

v.filtered = [i for i in filtered if i is not "" and i is not None]
print "set to v..writing now"
# print "\n".join([str((i, self.filtered[i])) for i in xrange(len(self.filtered)) if self.filtered[i] is not "" or self.filtered is not None])
v.filewriting()
print "written"
pool.close()
pool.join()
stop = timeit.default_timer()
print "Time taken: ", stop - start

