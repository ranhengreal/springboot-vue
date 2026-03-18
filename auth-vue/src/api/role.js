import request from '@/util/request.js'

export function listRoles(queryParam){
    return request.get('/role/list',{
        params: queryParam
    })
}

export function addRole(data){
    return request.post('/role',data)
}

export function updateRole(id,data){
    return request.put(`/role/${id}`,data)
}

export function deleteRoles(ids){
    let str = Array.isArray(ids) ? ids.join(',') : ids;
    return request.delete('/role/'+str)
}

//查询单个
export function getRole(id){
    return request.get(`/role/${id}`)
}

//授权
export function assignPermission(data){
    return request.post('/role/assign',data)
}