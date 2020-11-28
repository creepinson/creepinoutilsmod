package com.theoparis.creepinoutils.util.upgrade

import com.theoparis.creepinoutils.util.text.EnumColor
import com.theoparis.creepinoutils.util.text.LangUtils.localize
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.Constants.NBT
import java.util.*

class Upgrade internal constructor(private val name: String, val max: Int, val color: EnumColor) {
    companion object {
        val UPGRADES = HashMap<String, Upgrade>()
        val SPEED = Upgrade("speed", 25, EnumColor.RED)
        val ENERGY = Upgrade("energy", 25, EnumColor.BRIGHT_GREEN)
        val FILTER = Upgrade("filter", Int.MAX_VALUE, EnumColor.DARK_AQUA)
        val ANCHOR = Upgrade("anchor", 1, EnumColor.DARK_GREEN)
        fun buildMap(nbtTags: CompoundNBT?): Map<Upgrade, Int> {
            val upgrades: MutableMap<Upgrade, Int> = HashMap()
            if (nbtTags != null) {
                if (nbtTags.contains("upgrades")) {
                    val list = nbtTags.getList("upgrades", NBT.TAG_COMPOUND)
                    for (size in list.indices) {
                        val compound = list.getCompound(size)
                        val upgrade = upgradesArray[compound.getInt("type")]
                        upgrades[upgrade] = compound.getInt("amount")
                    }
                }
            }
            return upgrades
        }

        val upgradesArray: Array<Upgrade>
            get() = UPGRADES.values.toTypedArray()

        fun saveMap(upgrades: Map<Upgrade, Int>, nbtTags: CompoundNBT) {
            val list = ListNBT()
            for ((key, value) in upgrades) {
                list.add(getTagFor(key, value))
            }
            nbtTags.put("upgrades", list)
        }

        fun getTagFor(upgrade: Upgrade, amount: Int): CompoundNBT {
            val compound = CompoundNBT()
            compound.putString("type", upgrade.name)
            compound.putInt("amount", amount)
            return compound
        }

        init {
            UPGRADES[SPEED.name] = SPEED
        }
    }

    fun getName(): String {
        return localize("upgrade.$name")
    }

    val description: String
        get() = localize("upgrade.$name.desc")

    fun canMultiply(): Boolean {
        return max > 1
    }

    fun getInfo(tile: TileEntity): List<String> {
        val ret: List<String> = ArrayList()
        if (tile is IUpgradeable) {
            if (tile is IUpgradeInfoHandler) {
                return (tile as IUpgradeInfoHandler).getInfo(this)
            }
        }
        return ret
    }

    interface IUpgradeInfoHandler {
        fun getInfo(upgrade: Upgrade?): List<String>
    }
}