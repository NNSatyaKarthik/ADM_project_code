from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np
import pandas as pd
from scipy.io import mmwrite as matrix_write
import csv
from multiprocessing.dummy import Pool as ThreadPool

pool = ThreadPool(1000)


import timeit

start = timeit.default_timer()

#Your statements here


df = pd.read_csv("2015_08_000000000029")
comments = list(df.body)


filtered = []

def checkValid(i):
    body = str(i)
    cmm = ""
    for id in xrange(len(body)):
        i = body[id]

        if isinstance(i, unicode):
            cmm += str(i.encode('ascii', 'ignore'))
        else:
            cmm += str(i)

    filtered.append(cmm)

pool.map(checkValid,comments)


vectorizer = TfidfVectorizer(stop_words='english', norm='l2', sublinear_tf=True)
tfidf_matrix = vectorizer.fit_transform(filtered)


write = pd.DataFrame(data = tfidf_matrix.toarray())

write.to_csv("output_1.csv")


# f = open("output_1.csv", "wb")
# writer = csv.writer(f)
#
# def save(line):
#     writer.writerows(np.array(line))
#
# pool.map(save,tfidf_matrix.todense())
#
stop = timeit.default_timer()
print stop - start

