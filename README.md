# BedWarsEditAddon-Plugin
# BedWarsEditAddon插件

BedWarsEditAddon 是一个基于 BedWars1058-25.2 插件的附属插件，为 BedWars 游戏添加了多种新功能和改进。

BedWarsEditAddon is an extension plugin based on the BedWars1058-25.2 plugin, adding a variety of new features and improvements to the BedWars game.

## 功能特性 - Features

### 1. 资源转换为经验系统 - Resource Conversion to Experience System
- 将铁锭、金锭、绿宝石转换为对应的经验值
- 可在 `XpBw.yml` 中配置每种资源的经验值
- 可配置哪些类型的竞技场开启经验转换

- Convert iron ingots, gold ingots, and emeralds into corresponding experience points
- The experience value of each resource can be configured in `XpBw.yml`
- You can configure which types of arenas enable experience conversion

### 2. 加血事件系统 - Health Regeneration Event System
- 游戏开始后定时为所有玩家加血
- 支持1-2次加血，可自定义加血时间
- 加血时显示标题、副标题和聊天栏信息
- 可在 `HealthAdd.yml` 中修改加血时间和信息

- After the game starts, periodically heal all players
- Supports 1-2 rounds of healing, with customizable healing times
- Displays title, subtitle, and chat messages when healing
- Healing times and messages can be modified in `HealthAdd.yml`

### 3. 新增道具系统 - New Item System
- **自救平台**：右键烈焰棒生成粘液块平台，有20秒冷却
- **降落伞**：右键羽毛迅速向上飞行50格后缓慢下落
- **魔法羊毛**：右键附魔白色羊毛，延伸5格搭路
- **回城卷轴**：右键火药，5秒后传送回出生点，移动或再次右键取消

- **Self-Rescue Platform**: Right-click the Blaze Rod to generate a Slime Block platform, with a 20-second cooldown.
- **Parachute**: Right-click the Feather to quickly fly up 50 blocks, then slowly descend.
- **Magic Wool**: Right-click the Enchanted White Wool to extend it 5 blocks and create a path.
- **Return Scroll**: Right-click the Gunpowder to teleport back to your spawn point after 5 seconds; moving or right-clicking again will cancel it.

### 4. 搭桥蛋生成逻辑修改 - Bridge Egg Generation Logic Modification
- 水平投掷：距离玩家0.25格开始生成，宽度为2格
- 垂直投掷：距离玩家0.5格开始生成，体积为2x2xh

- Horizontal throw: starts generating at 0.25 blocks from the player, with a width of 2 blocks
- Vertical throw: starts generating at 0.5 blocks from the player, with a volume of 2x2xh

### 5. 战绩系统
- 每击杀一人加2战力，拆一床加6战力，最终击杀加5战力
- 完整的等级系统：坚韧黑铁→秩序白银→荣耀黄金→闪耀钻石→至尊合金
- 每个等级有不同的前缀和升级所需战力
- 升级时会显示标题提示
- 数据持久化存储

- Gain 2 combat power for each kill, 6 combat power for destroying a bed, and 5 combat power for the final kill
- Complete ranking system: Tenacious Black Iron → Orderly White Silver → Glorious Gold → Shining Diamond → Supreme Alloy
- Each rank has different prefixes and combat power requirements for promotion
- Titles are displayed as prompts when leveling up
- Data is persistently stored

### 6. 变量系统 - Variable system
实现了以下变量：
- `%bw1058_teampeople%` - 队伍人数
- `%bw1058_totalplaytime%` - 总游戏时间
- `%bw1058_firstplaytime%` - 第一次游玩时间
战绩系统相关变量（前缀、战力值等）：
- `%bw1058_fullprefix%` 玩家当前战绩完整前缀 例如：[坚韧黑铁-I] 
- `%bw1058_ladderprefix%` 玩家当前战绩阶梯级前缀 例如：[坚韧黑铁] 
- `%bw1058_levelprefix%` 玩家当前战绩等级前缀 例如：I 
- `%bw1058_numbercombatpower%` 玩家下一次升级所需战力(数字式) 例如：60/200 
- `%bw1058_gridcombatpower%` 玩家下一次升级所需战力(方格式) 例如：&7■■■&0■■■■■■■ 
- `%bw1058_nowcombatpower%` 玩家当前的战力值 例如：60
- `%bw1058_totalcombatpower%` 玩家累积获得的战力值 例如：120
- `%bw1058_nowlargepowervalue%` 玩家当前战绩等级的最大战力值 例如：200
- `%bw1058_ladder%` 玩家当前战绩所处阶梯 例如：坚韧黑铁

The following variables have been implemented:
- `%bw1058_teampeople%` - Number of team members
- `%bw1058_totalplaytime%` - Total playtime
- `%bw1058_firstplaytime%` - First playtime
Variables related to the achievement system (prefixes, combat power, etc.):
- `%bw1058_fullprefix%` - Player's full current achievement prefix, e.g., [Tenacious Iron-I]
- `%bw1058_ladderprefix%` - Player's current achievement ladder-level prefix, e.g., [Tenacious Iron]
- `%bw1058_levelprefix%` - Player's current achievement level prefix, e.g., I
- `%bw1058_numbercombatpower%` - Combat power required for the player's next level (numeric), e.g., 60/200
- `%bw1058_gridcombatpower%` - Combat power required for the player's next level (grid style), e.g., &7■■■&0■■■■■■■
- `%bw1058_nowcombatpower%` - Player's current combat power, e.g., 60
- `%bw1058_totalcombatpower%` - Total combat power accumulated by the player, e.g., 120
- `%bw1058_nowlargepowervalue%` - Maximum combat power for the player's current achievement level, e.g., 200
- `%bw1058_ladder%` - The ladder of the player's current achievement, e.g., Tenacious Iron

## 安装方法 - Installation method

1. 确保服务器已安装 BedWars1058-25.2 插件
2. 将 `BedWarsEditAddon.jar` 文件放入服务器的 `plugins` 文件夹
3. 启动服务器，插件会自动生成配置文件
4. 根据需要修改各配置文件中的参数
5. 重启服务器使配置生效

1. Make sure the server has the BedWars1058-25.2 plugin installed
2. Place the `BedWarsEditAddon.jar` file into the server's `plugins` folder
3. Start the server, the plugin will automatically generate configuration files
4. Modify the parameters in each configuration file as needed
5. Restart the server for the changes to take effect

## 配置文件 - Configuration File

插件生成以下配置文件：

- `Config.yml` - 主配置文件，控制各功能开关
- `XpBw.yml` - 资源转换为经验的配置
- `HealthAdd.yml` - 加血事件的配置
- `Shop.yml` - 商店和道具的配置
- `message.yml` - 消息配置
- `shop_items.yml` - 商店物品显示配置

The plugin generates the following configuration files:

- `Config.yml` - Main configuration file, controls the switches for various functions
- `XpBw.yml` - Configuration for converting resources into experience
- `HealthAdd.yml` - Configuration for health-adding events
- `Shop.yml` - Configuration for the shop and items
- `message.yml` - Message configuration
- `shop_items.yml` - Configuration for shop item display

## 适配版本 - Compatible version

- Minecraft 1.8
- Minecraft 1.12

## 依赖 - Dependence

- BedWars1058-25.2
- Spigot 1.8.8/1.12.2

## 作者 - Author

- **作者**：ya_xzer21145
- **版本**：1.0.0

- **Author**：ya_xzer21145
- **Version**：1.0.0

## 许可证 - License

本插件采用 MIT 许可证，详见 LICENSE 文件。

This plugin is licensed under the MIT License. See the LICENSE file for details.

## 反馈与支持 - Feedback and Support

如有任何问题或建议，欢迎在 GitHub 仓库中提交 issue 或 pull request。

If you have any questions or suggestions, feel free to submit an issue or pull request in the GitHub repository.

---

*祝您游戏愉快！*
*Have fun playing the game!*
