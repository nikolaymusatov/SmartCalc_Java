#ifndef TOKEN_H
#define TOKEN_H

#include <functional>
#include <iostream>
#include <variant>

namespace myNamespace {

/**
 * @brief The Prior enum is used to set a right priority to mathematical tokens.
 */
enum Prior { p0, p1, p2, p3, p4, p5 };

/**
 * @brief The Type enum is needed to set the type of token which is used to
 * check tokens sequence.
 */
enum Type {
  tOpenParanth,
  tClosedParanth,
  tNum,
  tBinOperator,
  tFunc,
  tokenTypes
};

/**
 * @brief The Token class is used to describe basic parts of expression.
 */
class Token {
 public:
  using unary_function = std::function<double(double)>;
  using binary_function = std::function<double(double, double)>;
  using func_variant = std::variant<unary_function, binary_function, nullptr_t>;

  /**
   * @brief Token default constructor.
   */
  Token() = default;

  /**
   * @brief Token parameterized constructor. Creates token with next paremeters.
   * @param str - const std::string type value, used to set the name to token.
   * @param val - double type value, used to set token's value
   * @param pr - enum Prior type value, used to set priority to token.
   * @param t - enum Type value, used to set type of token.
   * @param f - std::variant value, contains callback function of token.
   */
  Token(const std::string str, double val, Prior pr, Type t, func_variant f)
      : name(str), value(val), priority(pr), type(t), function(f) {}

  /**
   * @brief name - std::string type value. Containes name of token.
   */
  std::string name;

  /**
   * @brief value - double type value. Used to set value to tokens which contain
   * numbers and x-var. Other tokens have value = 0.
   */
  double value;

  /**
   * @brief priority - enum Prior type value. Used to contain priority to tokens
   * to set right calculation process.
   */
  Prior priority;

  /**
   * @brief type - enum Type type value. Contains type of token.
   */
  Type type;

  /**
   * @brief function - std::variant type value. Contains callback function for
   * calculation. Can be used to call functions with one (unary_function) and
   * two (binary_function) paramaters. Tokens of numbers and x-var contain
   * nullptr_t type.
   */
  func_variant function;
};

}  // namespace myNamespace

#endif  // TOKEN_H
