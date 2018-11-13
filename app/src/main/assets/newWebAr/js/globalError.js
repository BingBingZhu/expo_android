/**
 * 捕获全局错误输出显示
 * 
 * @param {string} errMsg 出错消息
 * @param {string} scriptURI 错误文件
 * @param {number} lineNumber 行号
 * @param {number} columnNumber 列号
 * @param {Object} errObj 出错详细信息
 */
window.onerror = function(errMsg, scriptURI, lineNumber, columnNumber) {
    console.log(errMsg);
    var logger = document.getElementById('log');
    logger.innerHTML = `
    Error: ${errMsg}<br>
    URI: ${scriptURI}<br>
    Line: ${lineNumber}<br>
    Col: ${columnNumber}
  `;
};
