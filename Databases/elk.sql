# 添加索引
PUT /movie_index

# 查看索引列表
GET /_cat/indices?v

# 删除索引
DELETE /movie_index

# index type document field
# 不写id会随机生成
PUT /movie_index/type/1
{ "id":1,
  "name":"operation red sea",
  "doubanScore":8.5,
  "actorList":[  
    {"id":1,"name":"zhang yi"},
    {"id":2,"name":"hai qing"},
    {"id":3,"name":"zhang han yu"}
  ]
}

PUT /movie_index/movie/2
{
  "id":2,
  "name":"operation meigong river",
  "doubanScore":8.0,
  "actorList":[  
    {"id":3,"name":"zhang han yu"}
  ]
}

PUT /movie_index/movie/3
{
  "id":3,
  "name":"incident red sea",
  "doubanScore":5.0,
  "actorList":[  
    {"id":4,"name":"zhang chen"}
  ]
}