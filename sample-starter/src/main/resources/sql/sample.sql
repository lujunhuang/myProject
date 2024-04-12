#todo 你的建表语句,包含索引


-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
                          `id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                          `userId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '购买人',
                          `skuId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'SkuId',
                          `amount` int(8) NULL DEFAULT NULL COMMENT '购买数量',
                          `money` bigint(10) NULL DEFAULT NULL COMMENT '购买金额',
                          `payTime` datetime NULL DEFAULT NULL COMMENT '购买时间',
                          `payStatus` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付状态',
                          `delFlag` tinyint(3) NULL DEFAULT NULL COMMENT '删除标志（0代表存在 1代表删除）',
                          `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
                          `createBy` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                          `updateBy` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
                          `updateTime` datetime NULL DEFAULT NULL COMMENT '修改时间',
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE INDEX `id_pk`(`id`) USING BTREE,
                          INDEX `userId_idx`(`userId`, `delFlag`) USING BTREE,
                          INDEX `skuId_idx`(`skuId`, `delFlag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
-- ----------------------------
    --考虑买家频繁查询我的订单，因此以买家ID作为分库键，取模分成多个库。
    --每个库中，根据订单ID进行分表，以订单ID取模分成10个表，将订单数据分散存储在不同的表中。
    --卖家查询时可以通过数据库复制等技术，将数据异步复制到备库中,卖家查询时可以直接查询备库
-- ----------------------------

