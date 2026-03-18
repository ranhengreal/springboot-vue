import request from '@/util/request.js'

export function listUsers(queryParam){
    return request.get('/user/list',{
        params: queryParam
    })
}

export function addUser(data){
    return request.post('/user',data)
}

export function updateUser(id,data){
    return request.put(`/user/${id}`,data)
}

export function deleteUsers(ids){
    let str = Array.isArray(ids) ? ids.join(',') : ids;
    return request.delete('/user/'+str)
}

//查询单个
export function getUser(id){
    return request.get(`/user/${id}`)
}