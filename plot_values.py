import pylab
from numpy import genfromtxt
data = genfromtxt("values_labeled.output.csv", delimiter=",")
pylab.scatter(data[:,0], data[:,1], c=data[:,2])
pylab.show()

