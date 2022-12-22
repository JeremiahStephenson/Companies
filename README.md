# Companies app challenge
The main branch contains my initial version of this challenge. 

After I finished it I thought about it some more and realized that it could be better if I write my own paging source. 
The problem with the original implementation is that on a cold start you will see the old data before the new data is populated. That may not be the best experience.
I went ahead and wrote a different version that uses a custom paging source that handles all the loading from remote and local. This is found in the branch `pagingSource`.
