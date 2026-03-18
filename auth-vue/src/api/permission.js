import request from '@/util/request.js'

export function getRouters(){
    return request.get('/permission/getRouters')
}

export function treelist() {
    return request.get('/permission/treelist')
}

export function listPermissionByRoleId(roleId) {
    return request.get('/permission/role/'+roleId)
}

export function listPermissions(queryParam){
    return request.get('/permission/list',{
        params: queryParam
    })
}

export function addPermission(data){
    return request.post('/permission',data)
}

export function updatePermission(id,data){
    return request.put(`/permission/${id}`,data)
}

export function deletePermissions(ids){
    let str = Array.isArray(ids) ? ids.join(',') : ids;
    return request.delete('/permission/'+str)
}

//查询单个
export function getPermission(id){
    return request.get(`/permission/${id}`)
}