import request from '@/util/request.js'

export function login(data){
    return request.post("/login",data)
}

export function changePwd(data){
    return request.post("/changePwd",data)
}