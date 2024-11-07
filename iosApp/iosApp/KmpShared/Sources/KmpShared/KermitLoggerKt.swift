//
//  KermitLoggerKt.swift
//  KmpShared
//
//  Created by hagihara tomoaki on 2024/11/07.
//
@preconcurrency import shared

/**
  sharedモジュールのKermitLoggerだとKermitLogger.shared.d()と書かなければならず面倒なので
 より簡単にログ書き込みができるようにするために実装
 */
public class KermitLoggerKt {
    private static let logger = KermitLogger.shared

    private init() {}

    public static func d(tag: String, message: @escaping () -> String) {
        logger.d(tag: tag, message: message)
    }

    public static func i(tag: String, message: @escaping () -> String) {
        logger.i(tag: tag, message: message)
    }

    public static func w(tag: String, message: @escaping () -> String) {
        logger.w(tag: tag, message: message)
    }

    public static func e(tag: String, message: @escaping () -> String) {
        logger.e(tag: tag, message: message)
    }
}
