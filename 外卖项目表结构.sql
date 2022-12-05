CREATE DATABASE `rabbitmq_dinner` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';


CREATE TABLE deliveryman (
	id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '骑手id',
	name varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
	status varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
	date datetime NULL DEFAULT NULL COMMENT '时间',
	PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- --------------------------
-- Records of deliveryman
-- --------------------------

INSERT INTO deliveryman VALUES ('1', '骑手1', 'AVAILABLE', '2022-12-01 12:16:30');

SET FOREIGN_KEY_CHECKS = 1;

--------------------------------------------------------------------------------
CREATE TABLE restaurant (
	id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '餐厅id',
	name varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
	address varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
	status varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
	settlement_id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算id',
	date datetime NULL DEFAULT NULL COMMENT '时间',
	PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- --------------------------
-- Records of restaurant
-- --------------------------

INSERT INTO restaurant VALUES ('1', '小杨生煎', '科技园', 'OPEN', '1', '2022-12-01 12:16:30');

SET FOREIGN_KEY_CHECKS = 1;




--------------------------------------------------------------------------------
CREATE TABLE product (
	id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品id',
	name varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
	price decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
	restaurant_id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '餐厅id',
	status varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
	date datetime NULL DEFAULT NULL COMMENT '时间',
	PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- --------------------------
-- Records of product
-- --------------------------

INSERT INTO product VALUES ('1', '炒米饭', 23.25, '1', 'AVAILABLE', '2022-12-01-15:26:30');

SET FOREIGN_KEY_CHECKS = 1;



--------------------------------------------------------------------------------
CREATE TABLE order_detail (
	id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单id',
	status varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
	address varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
	account_id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
	product_id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品id',
	deliveryman_id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '骑手id',
	settlement_id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算id',
	reward_id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '积分奖励id',
	price decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
	date datetime NULL DEFAULT NULL COMMENT '时间',
	PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- --------------------------
-- Records of order_detail
-- --------------------------

SET FOREIGN_KEY_CHECKS = 1;

--------------------------------------------------------------------------------
CREATE TABLE reward (
	id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖励id',
	order_id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单id',
	amount decimal(10, 2) NULL DEFAULT NULL COMMENT '积分量',
	status varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
	date datetime NULL DEFAULT NULL COMMENT '时间',
	PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- --------------------------
-- Records of reward
-- --------------------------

SET FOREIGN_KEY_CHECKS = 1;


--------------------------------------------------------------------------------
CREATE TABLE settlement (
	id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '结算id',
	order_id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单id',
	transaction_id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易id',
	amount decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
	status varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
	date datetime NULL DEFAULT NULL COMMENT '时间',
	PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- --------------------------
-- Records of settlement
-- --------------------------

SET FOREIGN_KEY_CHECKS = 1;


--------------------------------------------------------------------------------
CREATE TABLE trans_message (
	id varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息id',
	order_id varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
	service varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息发送的服务',
	type varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态类型',
	exchange varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交换机',
	routingkey varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由键',
	queue varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '队列',
	sequence int(0) NULL DEFAULT NULL COMMENT '发送次数',
	payload text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Message的JSON',
	date datetime(0) NULL DEFAULT NULL,
	PRIMARY KEY (id, service) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- --------------------------
-- Records of trans_message
-- --------------------------

SET FOREIGN_KEY_CHECKS = 1;