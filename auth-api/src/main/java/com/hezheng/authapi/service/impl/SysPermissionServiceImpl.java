package com.hezheng.authapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hezheng.authapi.entity.SysPermission;
import com.hezheng.authapi.mapper.SysPermissionMapper;
import com.hezheng.authapi.service.SysPermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {



    @Override
    public List<SysPermission> list(Wrapper<SysPermission> queryWrapper) {
        List<SysPermission> list = super.list(queryWrapper);
        return this.buildMenuTree(list);
    }
    @Override
    public List<SysPermission> selectPermissionList() {
        List<SysPermission> list = baseMapper.selectList(null);
        return this.buildMenuTree(list);
    }

    @Override
    public List<SysPermission> selectPermissionByUserId(Long userId) {
        // 1. 先获取用户拥有的权限ID列表
        List<Long> permissionIds = baseMapper.selectPermissionIdsByUserId(userId);

        // 2. 如果权限ID列表为空，直接返回空列表，避免 IN () 语法错误
        if (permissionIds == null || permissionIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 3. 根据权限ID列表查询权限信息
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", permissionIds);
        List<SysPermission> list = baseMapper.selectList(queryWrapper);

        // 4. 构建树形结构并返回
        return this.buildMenuTree(list);
    }

    private List<SysPermission> buildMenuTree(List<SysPermission> menus)
    {
        List<SysPermission> returnList = new ArrayList<SysPermission>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysPermission dept : menus)
        {
            tempList.add(dept.getId());
        }
        for (Iterator<SysPermission> iterator = menus.iterator(); iterator.hasNext();)
        {
            SysPermission menu = (SysPermission) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId()))
            {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = menus;
        }
        return returnList;
    }

    private void recursionFn(List<SysPermission> list, SysPermission t)
    {
        // 得到子节点列表
        List<SysPermission> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysPermission tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysPermission> getChildList(List<SysPermission> list, SysPermission t)
    {
        List<SysPermission> tlist = new ArrayList<SysPermission>();
        Iterator<SysPermission> it = list.iterator();
        while (it.hasNext())
        {
            SysPermission n = (SysPermission) it.next();
            if (n.getParentId() != null && n.getParentId().longValue() == t.getId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysPermission> list, SysPermission t)
    {
        return getChildList(list, t).size() > 0;
    }
}
