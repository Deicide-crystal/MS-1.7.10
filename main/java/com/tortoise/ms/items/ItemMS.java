package com.tortoise.ms.items;

import java.util.List;

import com.tortoise.ms.MSMod;
import com.tortoise.ms.entity.EntityRainbowLightningBolt;
import com.tortoise.ms.util.AntiDisarm;
import com.tortoise.ms.util.MSUtil;
import com.tortoise.ms.util.RainbowText;
import com.tortoise.ms.util.regen.RegenUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemMS extends ItemSword {
    /* 判断是否正在时停 */
    public static Boolean TimeStop = false;

    public ItemMS() {
        /** 使用钻石属性初始化剑类物品 Emerald是绿宝石 但是这里是钻石 */
        super(ToolMaterial.EMERALD);
        setUnlocalizedName("MS");
        setCreativeTab(MSMod.Tab);
    }

    /* 当持有该物品的实体更新 */
    @Override
    public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_,
            boolean p_77663_5_) {
        /* 判断持有物品者是不是玩家 */
        if (p_77663_3_ instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) p_77663_3_;
            player.capabilities.allowFlying = true;/* 设置玩家可以飞行(每次更新都会启用飞行,实际上设置一次就可以) */
            /* 判断玩家Y轴是否在-45以下 */
            if (player.posY <= -45.0D) {
                player.motionY += 10;
                /* 添加Y轴动量以拉回 */
            }
            if (!AntiDisarm.Contains(player.getCommandSenderName())) {
                AntiDisarm.Add(player.getCommandSenderName());
            }
        }
        super.onUpdate(p_77663_1_, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        /* 当物品被右击 */
        TimeStop = true;
        //
        int Sum = 60;
        // 园的半径
        int Range = 10;
        for (double Angle = 0; Angle < 60; Angle++) {
            // 计算角度
            double Rad = (360 * (Angle / Sum)) * Math.PI / 180;
            // 以玩家的x和y轴作为圆心,根据角度计算余弦和正弦,乘以Range(半径)得出坐标
            // 这两+ 和 - 都一样,自己画个图就理解了
            double X = p_77659_3_.posX + Math.sin(Rad) * Range;
            double Z = p_77659_3_.posZ + Math.cos(Rad) * Range;
            EntityRainbowLightningBolt Bolt = new EntityRainbowLightningBolt(p_77659_3_.worldObj, X,
                    p_77659_3_.posY - 1, Z);
            Bolt.worldObj.spawnEntityInWorld(Bolt);
        }
        for (int i = 0; i < p_77659_2_.loadedEntityList.size(); i++) {
            Entity e = (Entity) p_77659_2_.loadedEntityList.get(i);
            if (e != p_77659_3_) {/* 以防搞死自己 */
                if (e instanceof EntityLivingBase) {
                    EntityLivingBase Living = (EntityLivingBase) e;
                    Living.worldObj.loadedEntityList.remove(Living);
                    // Living.getEntityData().setBoolean("MSDead", true);
                    // Living.onDeath(DamageSource.outOfWorld);
                }
            }
        }
        /* 此处应有嘲讽代码,不过刚被我删了 */
        /* 效果如下输出所示 */
        return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
        RegenUtil.Regen(player.worldObj, (int) player.posX, (int) player.posZ);
        // 返回false代表丢不出去(Q键)
        return false;
    }

    /* 当取消蓄力(无论蓄力多久,包括单机右键) */
    @Override
    public void onPlayerStoppedUsing(ItemStack p_77615_1_, World p_77615_2_, EntityPlayer p_77615_3_, int p_77615_4_) {
        TimeStop = false;
        super.onPlayerStoppedUsing(p_77615_1_, p_77615_2_, p_77615_3_, p_77615_4_);

    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        /* 使用NBT判断这个实体是否被这个武器攻击过 */
        if (entity instanceof EntityLivingBase) {
            entity.getEntityData().setBoolean("MSDead", true);
            /*
             * 让实体掉落掉落物,如果是玩家发送死亡信息 泰坦生物重写了此方 ,但我们使用asm,可以不用 改血量就让实体GG
             */
            ((EntityLivingBase) entity).onDeath(DamageSource.outOfWorld);
            if (entity instanceof EntityPlayer) {/* EntityPlayer也继承EntityLivingBase */
                /* 可以写在if(entity instanceof EntityLivingBase) 外边,也可以写在里面 */
                MSUtil.ChatPrint((EntityPlayer) entity,
                        new StringBuilder().append(player.getCommandSenderName()).append(" LLLLL").toString());
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    /* 彩字每次调用的返回值都会不同,因此不能只调用一次 */
    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return RainbowText.Rainbow("MS-Sword");
    }

    /* 返回物品附魔特效 */
    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return EnumRarity.epic;
    }

    /* 获取物品是否有附魔特效 */
    @Override
    public boolean hasEffect(ItemStack par1ItemStack) {
        return true;
    }

    /* 说明右键蓄力时使用哪种动画 */
    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        /* 右键蓄力时像弓一样 */
        return EnumAction.bow;
    }

    @SuppressWarnings("all") // 这个警告。。。完全没有必要,因为无法解决
    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        p_77624_3_.add(RainbowText.Rainbow("描述..."));
        p_77624_3_.add(RainbowText.Rainbow("多行描述..."));
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
    }

    /* 当按下左键(也就是拿着这把剑的实体挥手时触发) */
    // getEntitiesWithinAABBExcludingEntity始终返回List<Entity>,搞不懂Bugjang为什么不指定泛型类型为Entity
    @SuppressWarnings("all")
    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        /* Vector 向量,这里表示生物朝向 */
        // 节约性能
        Vec3 Vector = entityLiving.getLookVec();// 获取实体朝向
        for (int i = 0; i < 512/* 512是攻击距离,遍历检测512个碰撞箱 */; i++) {
            double x = Vector.xCoord * i;
            double y = entityLiving.getEyeHeight() + Vector.yCoord * i;
            double z = Vector.zCoord * i;
            /* 根据碰撞箱获取实体 */
            List<Entity> Entities = entityLiving.worldObj.getEntitiesWithinAABBExcludingEntity(entityLiving/* 排除本身 */,
                    entityLiving.boundingBox.expand(3.0D, 3.0D, 3.0D).offset(x, y, z));
            /* entityLiving.boundingBox的minX,minY应当为玩家模型,此时把模型扩大3格,然后offset把位置调整到玩家位置 */
            /* 遍历3x3x3碰撞箱内的所有实体 */
            for (Entity e : Entities) {
                if (e instanceof EntityLivingBase) {
                    ((EntityLivingBase) e).onDeath(DamageSource.outOfWorld);/* 掉落掉落物 */
                    /* 因为挥手动作也可能由非玩家实体触发,所以判断挥手的是不是玩家 */
                    if (entityLiving instanceof EntityPlayer
                            && e instanceof EntityPlayer) {/* EntityPlayer也继承EntityLivingBase */
                        /* 可以写在if(entity instanceof EntityLivingBase) 外边,也可以写在里面 */
                        MSUtil.ChatPrint((EntityPlayer) e,
                                new StringBuilder().append(((EntityPlayer) entityLiving).getCommandSenderName())
                                        .append(" LLLLL").toString());
                    }
                }
                e.worldObj.removePlayerEntityDangerously(e);
                e.isDead = true;/* 如果isDead为true,则下一Tick移除 */
                /* removePlayerEntityDangerously里已经包含setDead,这里防止目标实体重写setDead */
            }
        }
        return super.onEntitySwing(entityLiving, stack);
    }
}