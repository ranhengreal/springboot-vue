import { useUserStore } from "@/store/user.js";

export default {
    mounted(el, binding, vnode) {
        const userStore = useUserStore();

        const permissions = userStore.permissions;
        const { value } = binding;
        if (value && value instanceof Array && value.length > 0) {
            const permissionFlag = value;
            const hasPermissions = permissions.some((permission) => {
                return permissionFlag.includes(permission);
            });

            if (!hasPermissions) {
                el.parentNode && el.parentNode.removeChild(el);
            }
        }
    },
};