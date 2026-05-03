(function () {
  function isObject(value) {
    return value !== null && typeof value === "object" && !Array.isArray(value);
  }

  function formatApiError(status, body) {
    if (!isObject(body)) {
      return "Request failed (" + status + ").";
    }

    if (typeof body.message === "string" && body.message.trim() !== "") {
      return body.message;
    }

    if (isObject(body.messages)) {
      return Object.entries(body.messages)
        .map(function (entry) {
          return entry[1];
        })
        .join(" · ");
    }

    if (typeof body.error === "string" && body.error.trim() !== "") {
      return body.error;
    }

    return "Request failed (" + status + ").";
  }

  function findErrorElement(form) {
    return (
      form.querySelector("[data-api-error]") ||
      form.parentElement?.querySelector("[data-api-error]") ||
      document.querySelector("[data-api-error]")
    );
  }

  function clearFieldErrors(form) {
    form.querySelectorAll("[data-field-error]").forEach(function (el) {
      el.textContent = "";
    });
  }

  function applyFieldErrors(form, body) {
    if (!isObject(body) || !isObject(body.messages)) {
      return false;
    }

    var hasAppliedAny = false;

    Object.entries(body.messages).forEach(function (entry) {
      var fieldName = entry[0];
      var message = entry[1];

      var fieldErrorEl = form.querySelector('[data-field-error="' + fieldName + '"]');
      if (fieldErrorEl) {
        fieldErrorEl.textContent = message;
        hasAppliedAny = true;
      }
    });

    return hasAppliedAny;
  }

  function setSubmittingState(form, isSubmitting) {
    var submitButtons = form.querySelectorAll('button[type="submit"], input[type="submit"]');

    submitButtons.forEach(function (btn) {
      btn.disabled = isSubmitting;
    });

    if (isSubmitting) {
      form.classList.add("is-submitting");
    } else {
      form.classList.remove("is-submitting");
    }
  }

  window.medicalRecordApi = {
    formatApiError: formatApiError,

    request: function (method, url, jsonBody) {
      return fetch(url, {
        method: method,
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json"
        },
        credentials: "same-origin",
        body: jsonBody === undefined || jsonBody === null
          ? undefined
          : JSON.stringify(jsonBody)
      }).then(function (res) {
        if (res.status === 204) {
          return null;
        }

        return res.text().then(function (text) {
          var data = null;

          if (text && text.trim() !== "") {
            try {
              data = JSON.parse(text);
            } catch (e) {
              data = null;
            }
          }

          if (!res.ok) {
            throw { status: res.status, body: data || {} };
          }

          return data;
        });
      });
    },

    bindJsonForm: function (form, buildPayload) {
      var errEl = findErrorElement(form);

      function showErr(msg) {
        if (!errEl) {
          return;
        }
        errEl.textContent = msg;
        errEl.classList.add("is-visible");
      }

      function clearErr() {
        if (!errEl) {
          return;
        }
        errEl.textContent = "";
        errEl.classList.remove("is-visible");
      }

      form.addEventListener("submit", function (e) {
        e.preventDefault();

        clearErr();
        clearFieldErrors(form);
        setSubmittingState(form, true);

        var method = form.getAttribute("data-method") || "POST";
        var action = form.getAttribute("data-action");
        var redirect = form.getAttribute("data-redirect");

        if (!action) {
          setSubmittingState(form, false);
          showErr("Form is missing data-action.");
          return;
        }

        var payload;
        try {
          payload = buildPayload(form);
        } catch (ex) {
          setSubmittingState(form, false);
          showErr(ex.message || "Invalid form data.");
          return;
        }

        window.medicalRecordApi
          .request(method, action, payload)
          .then(function () {
            if (redirect) {
              window.location.href = redirect;
            } else {
              window.location.reload();
            }
          })
          .catch(function (err) {
            var fieldErrorsApplied = applyFieldErrors(form, err.body);

            if (!fieldErrorsApplied) {
              var msg = formatApiError(err.status, err.body);
              showErr(msg);
            } else if (err.body && err.body.message) {
              showErr(err.body.message);
            }
          })
          .finally(function () {
            setSubmittingState(form, false);
          });
      });
    }
  };
})();