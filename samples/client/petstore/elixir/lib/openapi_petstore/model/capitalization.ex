# NOTE: This file is auto generated by OpenAPI Generator 7.5.0-SNAPSHOT (https://openapi-generator.tech).
# Do not edit this file manually.

defmodule OpenapiPetstore.Model.Capitalization do
  @moduledoc """
  
  """

  @derive Jason.Encoder
  defstruct [
    :smallCamel,
    :CapitalCamel,
    :small_Snake,
    :Capital_Snake,
    :SCA_ETH_Flow_Points,
    :ATT_NAME
  ]

  @type t :: %__MODULE__{
    :smallCamel => String.t | nil,
    :CapitalCamel => String.t | nil,
    :small_Snake => String.t | nil,
    :Capital_Snake => String.t | nil,
    :SCA_ETH_Flow_Points => String.t | nil,
    :ATT_NAME => String.t | nil
  }

  def decode(value) do
    value
  end
end

